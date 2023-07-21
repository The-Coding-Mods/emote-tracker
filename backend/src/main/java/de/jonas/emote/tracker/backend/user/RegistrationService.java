package de.jonas.emote.tracker.backend.user;

import static de.jonas.emote.tracker.backend.emote.SevenTVService.buildRegexString;

import com.github.twitch4j.chat.TwitchChat;
import com.github.twitch4j.helix.domain.User;
import de.jonas.emote.tracker.backend.emote.SevenTVService;
import de.jonas.emote.tracker.backend.model.database.Emote;
import de.jonas.emote.tracker.backend.model.database.EmoteCountMap;
import de.jonas.emote.tracker.backend.network.wrapper.TwitchApiWrapper;
import de.jonas.emote.tracker.backend.twitch.Client;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RegistrationService {
    private final TwitchApiWrapper twitchApi;
    private final TwitchChat chat;
    private final UserRepository userRepository;

    private final SevenTVService sevenTVService;

    public RegistrationService(TwitchApiWrapper twitchApi, Client client, UserRepository userRepository,
                               SevenTVService sevenTVService) {
        this.twitchApi = twitchApi;
        this.chat = client.getTwitchClient().getChat();
        this.userRepository = userRepository;
        this.sevenTVService = sevenTVService;
    }

    public boolean register(String username) {
        if (this.chat.isChannelJoined(username)) {
            return false;
        }
        this.chat.joinChannel(username);
        List<User> users =
            this.twitchApi.getUsers(null, Collections.singletonList(username)).getUsers();
        if (users.isEmpty()) {
            unregister(username);
            return false;
        }
        String userId = users.get(0).getId();
        if (Boolean.TRUE.equals(userRepository.existsUserByTwitchUserId(userId))) {
            return false;
        }
        List<Emote> emotes = sevenTVService.getSevenTVEmotes(userId);
        if (emotes.isEmpty()) {
            unregister(username);
            return false;
        }

        de.jonas.emote.tracker.backend.model.database.User
            user = new de.jonas.emote.tracker.backend.model.database.User()
            .setTwitchUserId(userId)
            .setUsername(username)
            .setSevenTVUserId(sevenTVService.get7TvUserOverview(userId).getUser().getId());
        // Save user already to have a UUID generated. Otherwise, the User reference in the EmoteMap is null
        user = userRepository.saveAndFlush(user);

        Set<EmoteCountMap> emoteCountMaps = EmoteCountMap.fromEmoteList(emotes, user);
        user.setEmoteCounts(emoteCountMaps);
        user.setEmoteRegex(buildRegexString(emotes));
        userRepository.saveAndFlush(user);


        return true;
    }

    public boolean unregister(String username) {
        return this.chat.leaveChannel(username);
    }

    @PreDestroy
    public void destroy() {
        for (String channel : this.chat.getChannels()) {
            this.chat.leaveChannel(channel);
        }
        this.chat.disconnect();
    }


    @PostConstruct
    public void initializeUsersAfterStart() {
        for (final var user : userRepository.findAll()) {
            log.info("Subscribe to already registered users twitch channel: {}", user.getUsername());
            register(user.getUsername());
        }

    }
}
