package de.jonas.emote.tracker.backend.emote;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.jonas.emote.tracker.backend.activity.ActivityService;
import de.jonas.emote.tracker.backend.database.Emote;
import de.jonas.emote.tracker.backend.database.OriginalEmote;
import de.jonas.emote.tracker.backend.database.Source;
import de.jonas.emote.tracker.backend.database.Streamer;
import de.jonas.emote.tracker.backend.database.UserEmote;
import de.jonas.emote.tracker.backend.model.origin.Data;
import de.jonas.emote.tracker.backend.model.origin.EmoteSet;
import de.jonas.emote.tracker.backend.model.origin.UserOverview7TV;
import de.jonas.emote.tracker.backend.network.wrapper.SevenTVApiWrapper;
import de.jonas.emote.tracker.backend.twitch.MessageHandler;
import de.jonas.emote.tracker.backend.user.UserRepository;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EmoteServiceTest {

    private SevenTVApiWrapper sevenTVApi;
    private UserRepository userRepository;
    private ActivityService activityService;
    private EmoteService emoteService;
    private EmoteRepository emoteRepository;

    private static List<de.jonas.emote.tracker.backend.model.origin.Emote> create7TVCustomEmote() {
        List<de.jonas.emote.tracker.backend.model.origin.Emote> emotes = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Data data = mock(Data.class);
            when(data.getName()).thenReturn("emote" + i);
            de.jonas.emote.tracker.backend.model.origin.Emote emote =
                mock(de.jonas.emote.tracker.backend.model.origin.Emote.class);
            when(emote.getId()).thenReturn("id" + i);
            when(emote.getName()).thenReturn("emote" + i + "_custom");
            when(emote.getData()).thenReturn(data);
            emotes.add(emote);
        }
        return emotes;
    }

    private static List<de.jonas.emote.tracker.backend.model.origin.Emote> create7TVOriginalEmote() {
        List<de.jonas.emote.tracker.backend.model.origin.Emote> emotes = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Data data = mock(Data.class);
            when(data.getName()).thenReturn("emote" + i);
            de.jonas.emote.tracker.backend.model.origin.Emote emote =
                mock(de.jonas.emote.tracker.backend.model.origin.Emote.class);
            when(emote.getId()).thenReturn("id" + i);
            when(emote.getName()).thenReturn("emote" + i);
            when(emote.getData()).thenReturn(data);
            emotes.add(emote);
        }
        return emotes;
    }

    private static List<de.jonas.emote.tracker.backend.model.origin.Emote> create7TVEmotes() {
        List<de.jonas.emote.tracker.backend.model.origin.Emote> emotes = new ArrayList<>();
        emotes.addAll(create7TVCustomEmote());
        emotes.addAll(create7TVOriginalEmote());
        return emotes;
    }

    private static Set<Emote> createUserEmote() {
        Set<Emote> emotes = new HashSet<>();
        for (int i = 0; i < 5; i++) {
            UserEmote emote = new UserEmote();
            emote.setId("id" + i);
            emote.setName("emote" + i + "_custom");
            emote.setSource(Source.SEVENTV);
            emotes.add(emote);
        }
        return emotes;
    }

    private static Set<Emote> createOriginalEmote() {
        Set<Emote> emotes = new HashSet<>();
        for (int i = 0; i < 5; i++) {
            OriginalEmote emote = new OriginalEmote();
            emote.setId("id" + i);
            emote.setName("emote" + i);
            emote.setSource(Source.SEVENTV);
            emotes.add(emote);
        }
        return emotes;
    }

    private static Set<Emote> createEmotes() {
        Set<Emote> emotes = new HashSet<>();
        emotes.addAll(createUserEmote());
        emotes.addAll(createOriginalEmote());
        return emotes;
    }

    @BeforeEach
    void setUp() {
        sevenTVApi = mock(SevenTVApiWrapper.class);
        userRepository = mock(UserRepository.class);
        activityService = mock(ActivityService.class);
        MessageHandler messageHandler = mock(MessageHandler.class);
        emoteRepository = mock(EmoteRepository.class);
        emoteService = new EmoteService(sevenTVApi, userRepository, activityService, messageHandler, emoteRepository);
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
        verify(activityService, times(0)).createEmoteUpdateActivity(any());
    }

    @Test
    void addEmotes_should_return_only_original_emotes() {
        String userId = "testUser";
        UserOverview7TV mockUserOverview = mock(UserOverview7TV.class);
        EmoteSet mockEmoteSet = mock(EmoteSet.class);
        List<de.jonas.emote.tracker.backend.model.origin.Emote> mockEmotes = create7TVOriginalEmote();
        when(mockUserOverview.getEmoteSet()).thenReturn(mockEmoteSet);
        when(mockEmoteSet.getEmotes()).thenReturn(mockEmotes);
        when(sevenTVApi.getUserByTwitchId(userId)).thenReturn(mockUserOverview);

        Set<Emote> emotes = emoteService.addEmotes(userId);

        assertThat(emotes)
            .hasSize(5)
            .hasOnlyElementsOfType(OriginalEmote.class)
            .extracting(Emote::getName)
            .containsExactlyInAnyOrder("emote0", "emote1", "emote2", "emote3", "emote4");
        verify(activityService, times(1)).createEmoteUpdateActivity(any());
    }

    @Test
    void addEmotes_should_return_only_custom_emotes() {
        String userId = "testUser";
        UserOverview7TV mockUserOverview = mock(UserOverview7TV.class);
        EmoteSet mockEmoteSet = mock(EmoteSet.class);
        List<de.jonas.emote.tracker.backend.model.origin.Emote> mockEmotes = create7TVCustomEmote();
        when(mockUserOverview.getEmoteSet()).thenReturn(mockEmoteSet);
        when(mockEmoteSet.getEmotes()).thenReturn(mockEmotes);
        when(sevenTVApi.getUserByTwitchId(userId)).thenReturn(mockUserOverview);

        Set<Emote> emotes = emoteService.addEmotes(userId);

        assertThat(emotes)
            .hasSize(5)
            .hasOnlyElementsOfType(UserEmote.class)
            .extracting(Emote::getName)
            .containsExactlyInAnyOrder("emote0_custom", "emote1_custom", "emote2_custom", "emote3_custom",
                "emote4_custom");
        verify(activityService, times(1)).createEmoteUpdateActivity(any());
    }

    @Test
    void addEmotes_should_return_custom_and_user_emotes() {
        String userId = "testUser";
        UserOverview7TV mockUserOverview = mock(UserOverview7TV.class);
        EmoteSet mockEmoteSet = mock(EmoteSet.class);
        List<de.jonas.emote.tracker.backend.model.origin.Emote> mockEmotes = create7TVEmotes();
        when(mockUserOverview.getEmoteSet()).thenReturn(mockEmoteSet);
        when(mockEmoteSet.getEmotes()).thenReturn(mockEmotes);
        when(sevenTVApi.getUserByTwitchId(userId)).thenReturn(mockUserOverview);

        Set<Emote> emotes = emoteService.addEmotes(userId);

        assertThat(emotes)
            .hasSize(10)
            .hasOnlyElementsOfTypes(UserEmote.class, OriginalEmote.class)
            .extracting(Emote::getName)
            .containsExactlyInAnyOrder("emote0_custom", "emote1_custom", "emote2_custom", "emote3_custom",
                "emote4_custom", "emote0", "emote1", "emote2", "emote3", "emote4");
    }

    @Test
    void updateUserEmotes_should_return_no_change() {
        String userId = "testUser";
        Streamer mockStreamer = mock(Streamer.class);
        UserOverview7TV mockUserOverview = mock(UserOverview7TV.class);
        EmoteSet mockEmoteSet = mock(EmoteSet.class);
        List<de.jonas.emote.tracker.backend.model.origin.Emote> mockEmotes = create7TVEmotes();
        when(mockUserOverview.getEmoteSet()).thenReturn(mockEmoteSet);
        when(mockEmoteSet.getEmotes()).thenReturn(mockEmotes);
        when(sevenTVApi.getUserByTwitchId(userId)).thenReturn(mockUserOverview);
        when(userRepository.getStreamerByTwitchUserId(userId)).thenReturn(mockStreamer);
        when(mockStreamer.getUserEmotes()).thenReturn(createEmotes());

        EmoteUpdateDTO emoteUpdateDTO = emoteService.updateUserEmotes(userId);
        assertThat(emoteUpdateDTO.added()).isEmpty();
        assertThat(emoteUpdateDTO.removed()).isEmpty();
        assertThat(emoteUpdateDTO.updated()).isEmpty();
    }

    @Test
    void updateUserEmotes_should_return_added() {
        String userId = "testUser";
        Streamer mockStreamer = mock(Streamer.class);
        UserOverview7TV mockUserOverview = mock(UserOverview7TV.class);
        EmoteSet mockEmoteSet = mock(EmoteSet.class);
        List<de.jonas.emote.tracker.backend.model.origin.Emote> mockEmotes = create7TVOriginalEmote();
        when(mockUserOverview.getEmoteSet()).thenReturn(mockEmoteSet);
        when(mockEmoteSet.getEmotes()).thenReturn(mockEmotes);
        when(sevenTVApi.getUserByTwitchId(userId)).thenReturn(mockUserOverview);
        when(userRepository.getStreamerByTwitchUserId(userId)).thenReturn(mockStreamer);
        when(mockStreamer.getUserEmotes()).thenReturn(Collections.emptySet());

        EmoteUpdateDTO emoteUpdateDTO = emoteService.updateUserEmotes(userId);
        assertThat(emoteUpdateDTO.added()).hasSize(5)
            .extracting(Emote::getName)
            .containsExactlyInAnyOrder("emote0", "emote1", "emote2", "emote3", "emote4");
        assertThat(emoteUpdateDTO.removed()).isEmpty();
        assertThat(emoteUpdateDTO.updated()).isEmpty();
    }

    @Test
    void updateUserEmotes_should_return_removed() {
        String userId = "testUser";
        Streamer mockStreamer = mock(Streamer.class);
        UserOverview7TV mockUserOverview = mock(UserOverview7TV.class);
        EmoteSet mockEmoteSet = mock(EmoteSet.class);
        List<de.jonas.emote.tracker.backend.model.origin.Emote> mockEmotes = create7TVOriginalEmote();
        when(mockUserOverview.getEmoteSet()).thenReturn(mockEmoteSet);
        when(mockEmoteSet.getEmotes()).thenReturn(mockEmotes);
        when(sevenTVApi.getUserByTwitchId(userId)).thenReturn(mockUserOverview);
        when(userRepository.getStreamerByTwitchUserId(userId)).thenReturn(mockStreamer);
        when(mockStreamer.getUserEmotes()).thenReturn(createEmotes());

        EmoteUpdateDTO emoteUpdateDTO = emoteService.updateUserEmotes(userId);
        assertThat(emoteUpdateDTO.added()).isEmpty();
        assertThat(emoteUpdateDTO.removed())
            .hasSize(5)
            .extracting(Emote::getName)
            .containsExactlyInAnyOrder("emote0_custom", "emote1_custom", "emote2_custom", "emote3_custom",
                "emote4_custom");
        assertThat(emoteUpdateDTO.updated()).isEmpty();
    }

    @Test
    void updateUserEmotes_should_return_updated() {
        String userId = "testUser";
        Streamer mockStreamer = mock(Streamer.class);
        UserOverview7TV mockUserOverview = mock(UserOverview7TV.class);
        EmoteSet mockEmoteSet = mock(EmoteSet.class);
        List<de.jonas.emote.tracker.backend.model.origin.Emote> mockEmotes = create7TVOriginalEmote();
        when(mockUserOverview.getEmoteSet()).thenReturn(mockEmoteSet);
        when(mockEmoteSet.getEmotes()).thenReturn(mockEmotes);
        when(sevenTVApi.getUserByTwitchId(userId)).thenReturn(mockUserOverview);
        when(userRepository.getStreamerByTwitchUserId(userId)).thenReturn(mockStreamer);
        when(mockStreamer.getUserEmotes()).thenReturn(createUserEmote());

        EmoteUpdateDTO emoteUpdateDTO = emoteService.updateUserEmotes(userId);
        assertThat(emoteUpdateDTO.added()).isEmpty();
        assertThat(emoteUpdateDTO.removed()).isEmpty();
        assertThat(emoteUpdateDTO.updated()).hasSize(5)
            .extracting(UpdatedEmote::getName, UpdatedEmote::getOldName)
            .containsExactlyInAnyOrder(
                tuple("emote0", "emote0_custom"),
                tuple("emote1", "emote1_custom"),
                tuple("emote2", "emote2_custom"),
                tuple("emote3", "emote3_custom"),
                tuple("emote4", "emote4_custom"));
    }
}
