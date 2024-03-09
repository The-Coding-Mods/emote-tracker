package de.jonas.emote.tracker.backend.user;

import de.jonas.emote.tracker.backend.database.Streamer;
import de.jonas.emote.tracker.backend.twitch.Client;
import de.jonas.emote.tracker.backend.twitch.MessageHandler;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RegistrationService {
    private final Client client;
    private final MessageHandler messageHandler;
    private final UserService userService;

    public RegistrationService(Client client, MessageHandler messageHandler, UserService userService) {
        this.client = client;
        this.messageHandler = messageHandler;
        this.userService = userService;
    }

    public void register(Streamer user) {
        this.client.joinChannel(user.getUsername());
        messageHandler.start(user.getTwitchUserId());
    }

    public boolean unregister(String username) {
        messageHandler.pause(username);
        return this.client.leaveChannel(username);
    }

    @PostConstruct
    public void initializeUsersAfterStart() {
        for (final var user : userService.getAllDatabaseUsers()) {
            log.info("Subscribe to already registered users twitch channel: {}", user.getUsername());
            register(user);
        }
    }
}
