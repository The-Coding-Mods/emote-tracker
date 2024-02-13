package de.jonas.emote.tracker.backend.user;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.jonas.emote.tracker.backend.database.Streamer;
import de.jonas.emote.tracker.backend.twitch.Client;
import de.jonas.emote.tracker.backend.twitch.MessageHandler;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RegistrationServiceTest {

    private Client client;
    private MessageHandler messageHandler;
    private UserService userService;
    private RegistrationService registrationService;

    @BeforeEach
    void setUp() {
        client = mock(Client.class);
        messageHandler = mock(MessageHandler.class);
        userService = mock(UserService.class);
        registrationService = new RegistrationService(client, messageHandler, userService);
    }

    @Test
    void register_should_successfully_register_in_client_and_handler() {
        Streamer streamer = mock(Streamer.class);
        when(streamer.getUsername()).thenReturn("testUser");
        when(streamer.getTwitchUserId()).thenReturn("testUserId");

        registrationService.register(streamer);

        verify(client, times(1)).joinChannel(streamer.getUsername());
        verify(messageHandler, times(1)).start(streamer.getTwitchUserId());
    }

    @Test
    void unregister_should_successfully_unregister_in_client_and_handler() {
        String username = "testUser";

        when(client.leaveChannel(username)).thenReturn(true);

        boolean result = registrationService.unregister(username);

        verify(client, times(1)).leaveChannel(username);
        verify(messageHandler, times(1)).pause(username);
        assertTrue(result);
    }

    @Test
    void unregister_should_fail_because_non_registered_user() {
        String username = "nonExistentUser";

        when(client.leaveChannel(username)).thenReturn(false);

        boolean result = registrationService.unregister(username);

        verify(messageHandler, times(1)).pause(username);
        verify(client, times(1)).leaveChannel(username);
        assertFalse(result);
    }

    @Test
    void shouldInitializeUsersAfterStart() {
        Streamer streamer1 = mock(Streamer.class);
        when(streamer1.getUsername()).thenReturn("testUser1");
        when(streamer1.getTwitchUserId()).thenReturn("testUserId1");

        Streamer streamer2 = mock(Streamer.class);
        when(streamer2.getUsername()).thenReturn("testUser2");
        when(streamer2.getTwitchUserId()).thenReturn("testUserId2");

        when(userService.getAll()).thenReturn(List.of(streamer1, streamer2));

        registrationService.initializeUsersAfterStart();

        verify(client, times(1)).joinChannel(streamer1.getUsername());
        verify(messageHandler, times(1)).start(streamer1.getTwitchUserId());

        verify(client, times(1)).joinChannel(streamer2.getUsername());
        verify(messageHandler, times(1)).start(streamer2.getTwitchUserId());
    }
}
