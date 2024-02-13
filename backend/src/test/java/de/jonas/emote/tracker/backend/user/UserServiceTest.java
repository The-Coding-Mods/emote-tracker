package de.jonas.emote.tracker.backend.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.github.twitch4j.helix.domain.User;
import com.github.twitch4j.helix.domain.UserList;
import de.jonas.emote.tracker.backend.api.model.EmoteCount;
import de.jonas.emote.tracker.backend.api.model.SimpleUser;
import de.jonas.emote.tracker.backend.database.Emote;
import de.jonas.emote.tracker.backend.database.Streamer;
import de.jonas.emote.tracker.backend.emote.EmoteService;
import de.jonas.emote.tracker.backend.emote.EmoteUpdateConverter;
import de.jonas.emote.tracker.backend.network.wrapper.TwitchApiWrapper;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserServiceTest {

    private TwitchApiWrapper twitchApi;
    private EmoteService emoteService;
    private UserRepository userRepository;
    private UserConverter userConverter;
    private EmoteUpdateConverter emoteUpdateConverter;
    private UserService userService;

    @BeforeEach
    void setUp() {
        twitchApi = mock(TwitchApiWrapper.class);
        emoteService = mock(EmoteService.class);
        userRepository = mock(UserRepository.class);
        userConverter = mock(UserConverter.class);
        emoteUpdateConverter = mock(EmoteUpdateConverter.class);
        userService = new UserService(twitchApi, emoteService, userRepository, userConverter, emoteUpdateConverter);
    }

    @Test
    void getById_should_return_optional_with_found_streamer() {
        Streamer streamer = mock(Streamer.class);
        when(userRepository.findById("123")).thenReturn(Optional.of(streamer));

        Optional<Streamer> result = userService.getById("123");

        assertTrue(result.isPresent());
        assertEquals(streamer, result.get());
    }

    @Test
    void getById_should_return_empty_optional() {
        when(userRepository.findById("123")).thenReturn(Optional.empty());

        Optional<Streamer> result = userService.getById("123");

        assertFalse(result.isPresent());
    }

    @Test
    void getSimpleUser_should_return_optional_with_user() {
        String userId = "123";
        Streamer streamer = mock(Streamer.class);
        SimpleUser simpleUser = mock(SimpleUser.class);
        when(userRepository.findById(userId)).thenReturn(Optional.of(streamer));
        when(userConverter.toSimpleUser(streamer)).thenReturn(simpleUser);

        Optional<SimpleUser> result = userService.getSimpleUser(userId);

        assertTrue(result.isPresent());
        assertEquals(simpleUser, result.get());
    }

    @Test
    void getSimpleUser_should_return_empty_optional() {
        String userId = "123";
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        Optional<SimpleUser> result = userService.getSimpleUser(userId);

        assertFalse(result.isPresent());
        verify(userConverter, times(0)).toSimpleUser(any());
    }

    @Test
    void existsByUserName_should_return_true_when_username_found() {
        String user = "username";

        when(userRepository.existsStreamerByUsername(user)).thenReturn(true);

        boolean result = userService.existsByUsername(user);

        assertTrue(result);
        verify(userRepository, times(1)).existsStreamerByUsername(user);
    }

    @Test
    void existsByUserName_should_return_false_when_username_not_found() {
        String user = "username";
        when(userRepository.existsStreamerByUsername(user)).thenReturn(false);

        boolean result = userService.existsByUsername(user);

        assertFalse(result);
        verify(userRepository, times(1)).existsStreamerByUsername(user);
    }

    @Test
    void create_should_create_and_save_new_user() {
        User user = mock(User.class);
        when(user.getId()).thenReturn("123");
        when(user.getProfileImageUrl()).thenReturn("profileImageUrl");
        UserList userList = mock(UserList.class);
        when(userList.getUsers()).thenReturn(Collections.singletonList(user));
        when(twitchApi.getUsers(null, Collections.singletonList("username"))).thenReturn(userList);
        when(emoteService.addEmotes("123")).thenReturn(Collections.emptySet());
        when(userRepository.saveAndFlush(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Streamer result = userService.create("username");

        assertNotNull(result);
        assertThat(result.getUsername()).isEqualTo("username");
        assertThat(result.getTwitchUserId()).isEqualTo("123");
        assertThat(result.getProfilePictureUrl()).isEqualTo("profileImageUrl");;
        assertThat(result.getUserEmotes()).isEmpty();
        verify(userRepository, times(1)).saveAndFlush(any());
    }

    @Test
    void create_should_throw_exception_when_twitch_user_not_found() {
        UserList userList = mock(UserList.class);
        when(userList.getUsers()).thenReturn(Collections.emptyList());
        when(twitchApi.getUsers(null, Collections.singletonList("username"))).thenReturn(userList);

        assertThrows(IllegalStateException.class, () -> userService.create("username"));
    }

    @Test
    void getEmotesWithNoUsage_should_return_emotes_with_count_is_zero() {
        Streamer streamer = mock(Streamer.class);
        when(userRepository.getEmotesWithNoUsageForStreamer(streamer)).thenReturn(List.of(new Emote().setId("123").setName("emote")));

        List<EmoteCount> result = userService.getEmotesWithNoUsage(streamer);

        assertThat(result).hasSize(1)
            .extracting(EmoteCount::getId, EmoteCount::getName, EmoteCount::getCount)
            .containsExactlyInAnyOrder(tuple("123", "emote", 0L));
    }
    @Test
    void getEmotesWithNoUsage_should_return_empty_list() {
        Streamer streamer = mock(Streamer.class);
        when(userRepository.getEmotesWithNoUsageForStreamer(streamer)).thenReturn(Collections.emptyList());

        List<EmoteCount> result = userService.getEmotesWithNoUsage(streamer);

        assertThat(result).isEmpty();
    }
}
