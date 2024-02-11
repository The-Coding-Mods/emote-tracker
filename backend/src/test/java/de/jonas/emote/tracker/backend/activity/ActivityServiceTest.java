package de.jonas.emote.tracker.backend.activity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.jonas.emote.tracker.backend.api.model.Emote;
import de.jonas.emote.tracker.backend.api.model.EmoteCount;
import de.jonas.emote.tracker.backend.database.Activity;
import de.jonas.emote.tracker.backend.database.Streamer;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ActivityServiceTest {
    private final ActivityRepository repositoryMock = mock(ActivityRepository.class);
    private final ActivityService activityService = new ActivityService(repositoryMock);

    private Streamer streamer;
    private de.jonas.emote.tracker.backend.database.Emote emote;
    private String userName;

    @BeforeEach
    void setUp() {
        streamer = new Streamer();
        emote = new de.jonas.emote.tracker.backend.database.Emote();
        userName = "testUser";
    }

    @Test
    void createEmoteUsageActivity_savesActivity() {
        activityService.createEmoteUsageActivity(streamer, emote, userName);
        verify(repositoryMock, times(1)).saveAndFlush(any(Activity.class));
    }

    @Test
    void createEmoteUpdateActivity_savesActivity() {
        activityService.createEmoteUpdateActivity(streamer);
        verify(repositoryMock, times(1)).saveAndFlush(any(Activity.class));
    }

    @Test
    void getActivitiesByStreamer_returnsExpectedActivities() {
        List<Activity> expectedActivities = Collections.singletonList(new Activity());
        when(repositoryMock.getActivitiesByStreamer(streamer)).thenReturn(expectedActivities);

        List<Activity> actualActivities = activityService.getActivitiesByStreamer(streamer);

        assertEquals(expectedActivities, actualActivities);
    }

    @Test
    void getTopEmotes_returnsExpectedEmotes() {
        List<EmoteCount> expectedEmotes = Collections.singletonList(new EmoteCount());
        when(repositoryMock.getEmoteUsageForStreamerDescending(streamer)).thenReturn(expectedEmotes);

        List<EmoteCount> actualEmotes = activityService.getTopEmotes(streamer);

        assertEquals(expectedEmotes, actualEmotes);
    }

    @Test
    void getBottomEmotes_returnsExpectedEmotes() {
        List<EmoteCount> expectedEmotes = Collections.singletonList(new EmoteCount());
        when(repositoryMock.getEmoteUsageForStreamerAscending(streamer)).thenReturn(expectedEmotes);

        List<EmoteCount> actualEmotes = activityService.getBottomEmotes(streamer);

        assertEquals(expectedEmotes, actualEmotes);
    }

    @Test
    void getLastUpdate_returnsExpectedTime() {
        Instant expectedTime = Instant.now();
        when(repositoryMock.getLastEmoteUpdate(streamer)).thenReturn(expectedTime);

        Instant actualTime = activityService.getLastUpdate(streamer);

        assertEquals(expectedTime, actualTime);
    }
}
