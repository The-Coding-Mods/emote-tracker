package de.jonas.emote.tracker.backend.user;

import de.jonas.emote.tracker.backend.api.controller.UserApi;
import de.jonas.emote.tracker.backend.api.model.Emote;
import de.jonas.emote.tracker.backend.database.Streamer;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController implements UserApi {
    private final RegistrationService registrationService;
    private final UserService userService;

    public UserController(RegistrationService registrationService,
                          UserService userService) {
        this.registrationService = registrationService;
        this.userService = userService;
    }

    @Override
    public ResponseEntity<List<Emote>> getBottomEmotes(String userId, Integer count) {
        return ResponseEntity.ok(userService.getBottomEmotes(userId, count));
    }

    @Override
    public ResponseEntity<List<Emote>> getTopEmotes(String userId, Integer count) {
        return ResponseEntity.ok(userService.getTopEmotes(userId, count));
    }

    @Override
    public ResponseEntity<List<Emote>> getEmotesWitNoUsage(String userId) {
        return ResponseEntity.ok(userService.getEmotesWithNrUsage(userId, 0));
    }

    @Override
    public ResponseEntity<List<Emote>> getAboveAverageEmotes(String userId) {
        return ResponseEntity.ok(userService.getEmotesAboveAverage(userId));
    }

    @Override
    public ResponseEntity<List<Emote>> getBelowAverageEmotes(String userId) {
        return ResponseEntity.ok(userService.getEmotesBelowAverage(userId));
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
        userService.updateEmotes(userId);
        return ResponseEntity.ok().build();
    }
}
