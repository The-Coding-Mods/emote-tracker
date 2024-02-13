package de.jonas.emote.tracker.backend.twitch;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import de.jonas.emote.tracker.backend.activity.ActivityService;
import de.jonas.emote.tracker.backend.database.Emote;
import de.jonas.emote.tracker.backend.database.Streamer;
import de.jonas.emote.tracker.backend.user.UserRepository;
import org.assertj.core.util.Sets;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MessageHandlerTest {

    private UserRepository userRepository;
    private ActivityService activityService;
    private MessageHandler messageHandler;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        activityService = mock(ActivityService.class);
        messageHandler = new MessageHandler(userRepository, activityService);
    }

    @Test
    void accept_should_not_process_message_when_paused() {
        ChannelMessageEvent event = mock(ChannelMessageEvent.class, RETURNS_DEEP_STUBS);
        when(event.getChannel().getId()).thenReturn("123");

        messageHandler.pause("123");
        messageHandler.accept(event);

        verify(userRepository, never()).getStreamerByTwitchUserId(anyString());
        verify(activityService, never()).createEmoteUsageActivity(any(), any(), any());
    }

    @Test
    void accept_should_process_message_when_not_paused() {
        ChannelMessageEvent event = mock(ChannelMessageEvent.class, RETURNS_DEEP_STUBS);
        when(event.getChannel().getId()).thenReturn("123");
        when(event.getMessage()).thenReturn("emote1 this is a message");
        when(event.getUser().getName()).thenReturn("user1");

        Streamer streamer = mock(Streamer.class);
        when(userRepository.getStreamerByTwitchUserId("123")).thenReturn(streamer);

        Emote emote = mock(Emote.class);
        when(emote.getName()).thenReturn("emote1");
        when(streamer.getUserEmotes()).thenReturn(Sets.set(emote));

        messageHandler.start("123");
        messageHandler.accept(event);

        verify(activityService).createEmoteUsageActivity(streamer, emote, "user1");
    }

    @Test
    void accept_should_not_create_activity_when_no_emote_in_message() {
        ChannelMessageEvent event = mock(ChannelMessageEvent.class, RETURNS_DEEP_STUBS);
        when(event.getChannel().getId()).thenReturn("123");
        when(event.getMessage()).thenReturn("emote2");
        when(event.getUser().getName()).thenReturn("user1");

        Streamer streamer = mock(Streamer.class);
        when(userRepository.getStreamerByTwitchUserId("123")).thenReturn(streamer);

        Emote emote = mock(Emote.class);
        when(emote.getName()).thenReturn("emote1");
        when(streamer.getUserEmotes()).thenReturn(Sets.set(emote));

        messageHandler.start("123");
        messageHandler.accept(event);

        verify(activityService, never()).createEmoteUsageActivity(any(), any(), any());
    }

    @Test
    void shouldPauseAndStartCorrectly() {
        String userId = "123";
        messageHandler.start(userId);
        assertThat(messageHandler.getIsPaused().get(userId)).isFalse();

        messageHandler.pause(userId);
        assertThat(messageHandler.getIsPaused().get(userId)).isTrue();
    }
}
