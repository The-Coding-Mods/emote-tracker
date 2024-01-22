package de.jonas.emote.tracker.backend.user;

import de.jonas.emote.tracker.backend.activity.ActivityService;
import de.jonas.emote.tracker.backend.api.controller.UserApi;
import de.jonas.emote.tracker.backend.api.model.Emote;
import de.jonas.emote.tracker.backend.database.Streamer;
import java.util.Collections;
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
    public ResponseEntity<List<Emote>> getBottomEmotes(String userId, Integer count) {
        Optional<Streamer> streamer = userService.getById(userId);
        if (streamer.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(activityService.getBottomEmotes(streamer.get()).subList(0, count));
    }

    @Override
    public ResponseEntity<List<Emote>> getTopEmotes(String userId, Integer count) {
        Optional<Streamer> streamer = userService.getById(userId);
        if (streamer.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(activityService.getTopEmotes(streamer.get()).subList(0, count));
    }

    @Override
    public ResponseEntity<List<Emote>> getEmotesWitNoUsage(String userId) {
        Optional<Streamer> streamer = userService.getById(userId);
        if (streamer.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(userService.getEmotesWithNoUsageForStreamer(streamer.get()));
    }

    @Override
    public ResponseEntity<List<Emote>> getAboveAverageEmotes(String userId) {
        return ResponseEntity.ok(Collections.emptyList());
    }

    @Override
    public ResponseEntity<List<Emote>> getBelowAverageEmotes(String userId) {
        return ResponseEntity.ok(Collections.emptyList());
    }

    @Override
    public ResponseEntity<String> getUserOverview(String userId) {
        return null;
    }

    @Override
    public ResponseEntity<String> registerNewUser(String user) {
        if (userService.exists(user)) {
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
    public ResponseEntity<Void> updateEmotesForUser(String userId) {
        log.info("Update emotes request for {}", userId);
        userService.updateEmotes(userId);
        return ResponseEntity.ok().build();
    }
}
