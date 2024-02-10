package de.jonas.emote.tracker.backend.user;

import com.github.twitch4j.helix.domain.User;
import de.jonas.emote.tracker.backend.api.model.Emote;
import de.jonas.emote.tracker.backend.api.model.SimpleUser;
import de.jonas.emote.tracker.backend.database.Streamer;
import de.jonas.emote.tracker.backend.emote.EmoteService;
import de.jonas.emote.tracker.backend.network.wrapper.TwitchApiWrapper;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final TwitchApiWrapper twitchApi;
    private final EmoteService emoteService;
    private final UserRepository userRepository;
    private final UserConverter userConverter;

    public UserService(TwitchApiWrapper twitchApi, EmoteService emoteService, UserRepository userRepository,
                       UserConverter userConverter) {
        this.twitchApi = twitchApi;
        this.emoteService = emoteService;
        this.userRepository = userRepository;
        this.userConverter = userConverter;
    }

    public Optional<Streamer> getById(String userId) {
        return userRepository.findById(userId);
    }

    public Optional<SimpleUser> getSimpleUser(String userId) {
        Optional<Streamer> streamer = getById(userId);
        return streamer.map(userConverter::toSimpleUser);
    }

    public boolean exists(String username) {
        return userRepository.existsStreamerByUsername(username);
    }

    public List<Streamer> getAll() {
        return userRepository.findAll();
    }

    public Streamer create(String user) throws IllegalStateException {
        List<User> users = this.twitchApi.getUsers(null, Collections.singletonList(user)).getUsers();
        if (users.isEmpty()) {
            throw new IllegalStateException("No twitch user found");
        }
        Streamer streamer = new Streamer()
            .setUsername(user)
            .setTwitchUserId(users.get(0).getId())
            .setRegistered(Instant.now())
            .setProfilePictureUrl(users.get(0).getProfileImageUrl())
            .setUserEmotes(emoteService.addEmotes(users.get(0).getId()));

        return userRepository.saveAndFlush(streamer);
    }

    public void updateEmotes(String userId) {
        emoteService.updateUserEmotes(userId);
    }

    /**
     * Retrieves a list of emotes that are not used in a specific streamer.
     *
     * @param streamer The streamer for whom to retrieve the unused emotes.
     * @return A list of Emote objects representing the emotes not used in the streamer.
     */
    public List<Emote> getEmotesWithNoUsageForStreamer(Streamer streamer) {
        return userRepository.getEmotesWithNoUsageForStreamer(streamer)
            .stream()
            .map(emote -> new Emote()
                .name(emote.getName())
                .id(emote.getId())
                .count(0L))
            .toList();
    }
}
