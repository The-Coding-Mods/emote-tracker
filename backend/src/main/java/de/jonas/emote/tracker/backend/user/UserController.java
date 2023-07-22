package de.jonas.emote.tracker.backend.user;

import de.jonas.emote.tracker.backend.api.controller.UserApi;
import de.jonas.emote.tracker.backend.api.model.Emote;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController implements UserApi {
    private final RegistrationService registrationService;
    private final UserService userService;
    private final EmoteUpdateService updateService;

    public UserController(RegistrationService registrationService,
                          UserService userService,
                          EmoteUpdateService updateService) {
        this.registrationService = registrationService;
        this.userService = userService;
        this.updateService = updateService;
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
    public ResponseEntity<String> getUserOverview(String userId) {
        return null;
    }

    @Override
    public ResponseEntity<String> registerNewUser(String user) {
        return registrationService.register(user)
            ? ResponseEntity.ok().build()
            : ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
    }

    @Override
    public ResponseEntity<String> unregisterUser(String user) {
        return registrationService.unregister(user)
            ? ResponseEntity.ok().build()
            : ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
    }

    @Override
    public ResponseEntity<Void> updateEmotesForUser(String userId) {
        updateService.updateUserEmotes(userId);
        return ResponseEntity.ok().build();
    }
}
