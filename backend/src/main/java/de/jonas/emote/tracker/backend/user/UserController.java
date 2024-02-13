package de.jonas.emote.tracker.backend.user;

import de.jonas.emote.tracker.backend.activity.ActivityService;
import de.jonas.emote.tracker.backend.api.controller.UserApi;
import de.jonas.emote.tracker.backend.api.model.EmoteCount;
import de.jonas.emote.tracker.backend.api.model.EmoteUpdateLog;
import de.jonas.emote.tracker.backend.api.model.SimpleUser;
import de.jonas.emote.tracker.backend.database.Streamer;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class UserController implements UserApi {
    private final RegistrationService registrationService;
    private final UserService userService;
    private final ActivityService activityService;

    public UserController(RegistrationService registrationService,
                          UserService userService, ActivityService activityService) {
        this.registrationService = registrationService;
        this.userService = userService;
        this.activityService = activityService;
    }

    @Override
    public ResponseEntity<List<EmoteCount>> getBottomEmotes(String userId, Integer count) {
        Optional<Streamer> streamer = userService.getById(userId);
        if (streamer.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        List<EmoteCount> emotes = activityService.getBottomEmotes(streamer.get());
        emotes.addAll(0, userService.getEmotesWithNoUsage(streamer.get()));
        return ResponseEntity.ok(emotes.subList(0, Math.min(count, emotes.size())));
    }

    @Override
    public ResponseEntity<List<EmoteCount>> getTopEmotes(String userId, Integer count) {
        Optional<Streamer> streamer = userService.getById(userId);
        if (streamer.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        List<EmoteCount> emotes = activityService.getTopEmotes(streamer.get());
        return ResponseEntity.ok(emotes.subList(0, Math.min(count, emotes.size())));
    }

    @Override
    public ResponseEntity<SimpleUser> getUser(String userId) {
        Optional<SimpleUser> user = userService.getSimpleUser(userId);
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(user.get());
    }

    @Override
    public ResponseEntity<String> registerNewUser(String user) {
        if (userService.existsByUsername(user)) {
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
        }
        try {
            Streamer streamer = userService.create(user);
            registrationService.register(streamer);

        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<String> unregisterUser(String user) {
        return registrationService.unregister(user)
            ? ResponseEntity.ok().build()
            : ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
    }

    @Override
    public ResponseEntity<EmoteUpdateLog> updateEmotesForUser(String userId) {
        log.info("Update emotes request for {}", userId);
        if (!userService.existsById(userId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(userService.updateEmotes(userId));
    }
}
