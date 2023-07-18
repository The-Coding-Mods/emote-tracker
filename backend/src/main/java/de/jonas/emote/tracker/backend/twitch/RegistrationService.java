package de.jonas.emote.tracker.backend.twitch;

import com.github.twitch4j.chat.TwitchChat;
import com.github.twitch4j.helix.domain.User;
import de.jonas.emote.tracker.backend.model.database.Emote;
import de.jonas.emote.tracker.backend.model.database.EmoteCountMap;
import de.jonas.emote.tracker.backend.model.database.Source;
import de.jonas.emote.tracker.backend.model.origin.UserOverview7TV;
import de.jonas.emote.tracker.backend.network.wrapper.SevenTVApiWrapper;
import de.jonas.emote.tracker.backend.network.wrapper.TwitchApiWrapper;
import de.jonas.emote.tracker.backend.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RegistrationService {
    private final SevenTVApiWrapper sevenTVApi;
    private final TwitchApiWrapper twitchApi;
    private final TwitchChat chat;
    private final UserRepository userRepository;

    public RegistrationService(
        SevenTVApiWrapper sevenTVApi,
        TwitchApiWrapper twitchApi,
        Client client,
        UserRepository userRepository) {
        this.sevenTVApi = sevenTVApi;
        this.twitchApi = twitchApi;
        this.chat = client.getTwitchClient().getChat();
        this.userRepository = userRepository;
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
        if (Boolean.TRUE.equals(userRepository.existsUserByTwitchUserId(users.get(0).getId()))) {
            return false;
        }
        if (!getSevenTVEmotes(username, users.get(0).getId())) {
            unregister(username);
            return false;
        }
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

    private boolean getSevenTVEmotes(String username, String userId) {
        if (userId == null) {
            return false;
        }
        UserOverview7TV overview = sevenTVApi.getUserByTwitchId(userId);
        List<Emote> emotes =
            overview.getEmoteSet().getEmotes().stream()
                .map(emote -> new Emote()
                    .setId(emote.getId())
                    .setName(emote.getName())
                    .setSource(Source.SEVENTV))
                .toList();

        de.jonas.emote.tracker.backend.model.database.User dbUser =
            new de.jonas.emote.tracker.backend.model.database.User()
                .setTwitchUserId(userId)
                .setUsername(username)
                .setSevenTVUserId(overview.getUser().getId());
        // Save de.jonas.emote.tracker.backend.user already to have a UUID generated. Otherwise, the User reference in the EmoteMap is null
        dbUser = userRepository.saveAndFlush(dbUser);

        Set<EmoteCountMap> emoteCountMaps = EmoteCountMap.fromEmoteList(emotes, dbUser);
        dbUser.setEmoteCounts(emoteCountMaps);
        String regex = "(?:^|(?<=\\\\s))(";
        regex += String.join("|", emotes.stream().map(Emote::getName).collect(Collectors.toSet()));
        regex += ")(?:$|(?=\\\\s))";
        dbUser.setEmoteRegex(regex);
        userRepository.saveAndFlush(dbUser);

        return true;
    }

    @PostConstruct
    public void initializeUsersAfterStart() {
        for (final var user : userRepository.findAll()) {
            log.info("Subscribe to already registered users twitch channel: {}", user.getUsername());
            register(user.getUsername());
        }

    }
}
