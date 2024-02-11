package de.jonas.emote.tracker.backend.emote;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.jonas.emote.tracker.backend.activity.ActivityService;
import de.jonas.emote.tracker.backend.database.Emote;
import de.jonas.emote.tracker.backend.database.Streamer;
import de.jonas.emote.tracker.backend.model.origin.EmoteSet;
import de.jonas.emote.tracker.backend.model.origin.UserOverview7TV;
import de.jonas.emote.tracker.backend.network.wrapper.SevenTVApiWrapper;
import de.jonas.emote.tracker.backend.twitch.MessageHandler;
import de.jonas.emote.tracker.backend.user.UserRepository;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@Disabled
class EmoteServiceTest {

    private SevenTVApiWrapper sevenTVApi;
    private UserRepository userRepository;
    private ActivityService activityService;
    private MessageHandler messageHandler;
    private EmoteService emoteService;

    @BeforeEach
    void setUp() {
        sevenTVApi = mock(SevenTVApiWrapper.class);
        userRepository = mock(UserRepository.class);
        activityService = mock(ActivityService.class);
        messageHandler = mock(MessageHandler.class);
        emoteService = new EmoteService(sevenTVApi, userRepository, activityService, messageHandler);
    }

    @Test
    void addEmotes_whenNoEmotesFound_throwsException() {
        String userId = "testUser";

        UserOverview7TV mockUserOverview = mock(UserOverview7TV.class);
        EmoteSet mockEmoteSet = mock(EmoteSet.class);
        when(mockUserOverview.getEmoteSet()).thenReturn(mockEmoteSet);
        when(mockEmoteSet.getEmotes()).thenReturn(Collections.emptyList());
        when(sevenTVApi.getUserByTwitchId(userId)).thenReturn(mockUserOverview);

        assertThrows(IllegalStateException.class, () -> emoteService.addEmotes(userId));
    }

    @Test
    void updateUserEmotes_happyPath() {
        String userId = "testUser";
        Streamer streamer = new Streamer();
        Set<Emote> emotes = new HashSet<>(Collections.singletonList(new Emote()));

        UserOverview7TV mockUserOverview = mock(UserOverview7TV.class);
        EmoteSet mockEmoteSet = mock(EmoteSet.class);
        when(mockUserOverview.getEmoteSet()).thenReturn(mockEmoteSet);
        when(mockEmoteSet.getEmotes()).thenReturn(Collections.singletonList(new de.jonas.emote.tracker.backend.model.origin.Emote())); // Add this line
        when(sevenTVApi.getUserByTwitchId(userId)).thenReturn(mockUserOverview);

        when(userRepository.getStreamerByTwitchUserId(userId)).thenReturn(streamer);
        when(emoteService.addEmotes(userId)).thenReturn(emotes);

        emoteService.updateUserEmotes(userId);

        verify(messageHandler, times(1)).pause(userId);
        verify(messageHandler, times(1)).start(userId);
        verify(userRepository, times(1)).saveAndFlush(streamer);
    }

    @Test
    void updateUserEmotes_whenNoEmotesFound() {
        String userId = "testUser";
        Streamer streamer = new Streamer();
        Set<Emote> emotes = new HashSet<>();

        UserOverview7TV mockUserOverview = mock(UserOverview7TV.class);
        EmoteSet mockEmoteSet = mock(EmoteSet.class);
        when(mockUserOverview.getEmoteSet()).thenReturn(mockEmoteSet);
        when(sevenTVApi.getUserByTwitchId(userId)).thenReturn(mockUserOverview);

        when(userRepository.getStreamerByTwitchUserId(userId)).thenReturn(streamer);
        when(emoteService.addEmotes(userId)).thenReturn(emotes);

        emoteService.updateUserEmotes(userId);

        verify(messageHandler, times(1)).pause(userId);
        verify(messageHandler, times(1)).start(userId);
        verify(userRepository, times(1)).saveAndFlush(streamer);
    }
}
