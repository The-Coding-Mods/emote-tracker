package de.jonas.emote.tracker.backend.user;

import static de.jonas.emote.tracker.backend.emote.SevenTVService.buildRegexString;

import com.github.twitch4j.helix.domain.User;
import de.jonas.emote.tracker.backend.emote.SevenTVService;
import de.jonas.emote.tracker.backend.model.database.Emote;
import de.jonas.emote.tracker.backend.model.database.EmoteCountMap;
import de.jonas.emote.tracker.backend.model.database.Streamer;
import de.jonas.emote.tracker.backend.network.wrapper.TwitchApiWrapper;
import de.jonas.emote.tracker.backend.twitch.Client;
import de.jonas.emote.tracker.backend.twitch.MessageHandler;
import jakarta.annotation.PostConstruct;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RegistrationService {
    private final TwitchApiWrapper twitchApi;
    private final Client client;
    private final UserRepository userRepository;

    private final SevenTVService sevenTVService;

    private final MessageHandler messageHandler;

    public RegistrationService(TwitchApiWrapper twitchApi, Client client, UserRepository userRepository,
                               SevenTVService sevenTVService, MessageHandler messageHandler) {
        this.twitchApi = twitchApi;
        this.client = client;
        this.userRepository = userRepository;
        this.sevenTVService = sevenTVService;
        this.messageHandler = messageHandler;
    }

    public boolean register(String username) {
        if (this.client.isChannelJoined(username)) {
            return false;
        }
        this.client.joinChannel(username);
        List<User> users =
            this.twitchApi.getUsers(null, Collections.singletonList(username)).getUsers();
        if (users.isEmpty()) {
            unregister(username);
            return false;
        }
        String userId = users.get(0).getId();
        if (Boolean.TRUE.equals(userRepository.existsStreamerByTwitchUserId(userId))) {
            return false;
        }
        List<Emote> emotes = sevenTVService.getSevenTVEmotes(userId);
        if (emotes.isEmpty()) {
            unregister(username);
            return false;
        }

        Streamer
            user = new Streamer()
            .setTwitchUserId(userId)
            .setUsername(username)
            .setSevenTVUserId(sevenTVService.get7TvUserOverview(userId).getUser().getId());
        // Save user already to have a UUID generated. Otherwise, the User reference in the EmoteMap is null
        user = userRepository.saveAndFlush(user);

        Set<EmoteCountMap> emoteCountMaps = EmoteCountMap.fromEmoteList(emotes, user);
        user.setEmoteCounts(emoteCountMaps);
        user.setEmoteRegex(buildRegexString(emotes));
        userRepository.saveAndFlush(user);

        messageHandler.start(userId);
        return true;
    }

    public boolean unregister(String username) {
        return this.client.leaveChannel(username);
    }


    @PostConstruct
    public void initializeUsersAfterStart() {
        for (final var user : userRepository.findAll()) {
            log.info("Subscribe to already registered users twitch channel: {}", user.getUsername());
            register(user.getUsername());
            messageHandler.start(user.getTwitchUserId());
        }

    }
}
