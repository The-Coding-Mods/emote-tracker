package de.jonas.emote.tracker.backend.user;

import com.github.twitch4j.helix.domain.User;
import de.jonas.emote.tracker.backend.database.Streamer;
import de.jonas.emote.tracker.backend.emote.EmoteService;
import de.jonas.emote.tracker.backend.network.wrapper.TwitchApiWrapper;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final TwitchApiWrapper twitchApi;
    private final EmoteService emoteService;
    private final UserRepository userRepository;

    public UserService(TwitchApiWrapper twitchApi, EmoteService emoteService, UserRepository userRepository) {
        this.twitchApi = twitchApi;
        this.emoteService = emoteService;
        this.userRepository = userRepository;
    }

    public Optional<Streamer> getById(String userId) {
        return userRepository.findById(userId);
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
            .setUserEmotes(emoteService.addEmotes(users.get(0).getId()));

        return userRepository.saveAndFlush(streamer);
    }

    public void updateEmotes(String userId) {
        emoteService.updateUserEmotes(userId);
    }
}
