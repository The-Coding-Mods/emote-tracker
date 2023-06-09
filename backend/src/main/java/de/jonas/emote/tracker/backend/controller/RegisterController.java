package de.jonas.emote.tracker.backend.controller;

import de.jonas.emote.tracker.backend.api.controller.UserApi;
import de.jonas.emote.tracker.backend.twitch.RegistrationService;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegisterController implements UserApi {

    private final RegistrationService registrationService;

    public RegisterController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @Override
    public ResponseEntity<String> getUserOverview(String userId) {
        return null;
    }

    @Override
    public ResponseEntity<String> registerNewUser(
        @RequestParam(value = "user") @NotNull String user) {
        return registrationService.register(user)
            ? ResponseEntity.ok().build()
            : ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
    }

    @Override
    public ResponseEntity<String> unregisterUser(@RequestParam(value = "user") @NotNull String user) {
        return registrationService.unregister(user)
            ? ResponseEntity.ok().build()
            : ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
    }
}
