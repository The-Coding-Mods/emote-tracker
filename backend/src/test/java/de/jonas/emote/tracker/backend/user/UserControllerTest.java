package de.jonas.emote.tracker.backend.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import de.jonas.emote.tracker.backend.activity.ActivityService;
import de.jonas.emote.tracker.backend.api.model.EmoteCount;
import de.jonas.emote.tracker.backend.api.model.EmoteUpdateLog;
import de.jonas.emote.tracker.backend.api.model.SimpleUser;
import de.jonas.emote.tracker.backend.database.Streamer;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class UserControllerTest {
    private UserController userController;
    private RegistrationService registrationService;
    private UserService userService;
    private ActivityService activityService;


    private static List<EmoteCount> createEmountCountList(int size) {
        return createEmountCountList(size, false);
    }

    private static List<EmoteCount> createEmountCountList(int size, boolean descending) {
        List<EmoteCount> emotes = Lists.newArrayList();
        for (int i = 0; i < size; i++) {
            emotes.add(new EmoteCount().id("100" + i).name("emote" + i).count((long) i + 1));
        }
        if (descending) {
            Collections.reverse(emotes);
        }
        return emotes;
    }

    private static List<EmoteCount> createEmotesWithZeroCount(int size) {
        List<EmoteCount> emotes = Lists.newArrayList();
        for (int i = 0; i < size; i++) {
            emotes.add(new EmoteCount().id("100" + i).name("zero" + i).count(0L));
        }
        return emotes;
    }

    @BeforeEach
    void setup() {
        registrationService = Mockito.mock(RegistrationService.class);
        userService = Mockito.mock(UserService.class);
        activityService = Mockito.mock(ActivityService.class);
        userController = new UserController(registrationService, userService, activityService);
    }


    @Test
    void getBottomEmotes_should_return_not_found_when_user_does_not_exist() {
        String userId = "1";

        when(userService.getById(userId)).thenReturn(Optional.empty());

        ResponseEntity<List<EmoteCount>> response = userController.getBottomEmotes(userId, 2);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void getBottomEmotes_should_return_bottom_emotes_when_user_exists_limited_to_provided_count() {
        String userId = "1";
        Streamer streamer = new Streamer();

        when(userService.getById(userId)).thenReturn(Optional.of(streamer));
        when(activityService.getBottomEmotes(streamer)).thenReturn(createEmountCountList(10));
        when(userService.getEmotesWithNoUsage(streamer)).thenReturn(Collections.emptyList());

        ResponseEntity<List<EmoteCount>> response = userController.getBottomEmotes(userId, 2);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody())
            .isNotNull()
            .hasSize(2)
            .extracting(EmoteCount::getName, EmoteCount::getCount)
            .containsExactlyInAnyOrder(
                tuple("emote0", 1L),
                tuple("emote1", 2L));
    }

    @Test
    void getBottomEmotes_should_return_emotes_with_no_usage_when_user_exists() {
        String userId = "1";
        Streamer streamer = new Streamer();

        when(userService.getById(userId)).thenReturn(Optional.of(streamer));
        when(activityService.getBottomEmotes(streamer)).thenReturn(createEmountCountList(5));
        when(userService.getEmotesWithNoUsage(streamer)).thenReturn(createEmotesWithZeroCount(2));

        ResponseEntity<List<EmoteCount>> response = userController.getBottomEmotes(userId, 10);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody())
            .isNotNull()
            .hasSize(7)
            .extracting(EmoteCount::getName, EmoteCount::getCount)
            .containsExactlyInAnyOrder(
                tuple("zero0", 0L),
                tuple("zero1", 0L),
                tuple("emote0", 1L),
                tuple("emote1", 2L),
                tuple("emote2", 3L),
                tuple("emote3", 4L),
                tuple("emote4", 5L));
    }


    @Test
    void getTopEmotes_should_return_not_found_when_user_does_not_exist() {
        String userId = "1";

        when(userService.getById(userId)).thenReturn(Optional.empty());

        ResponseEntity<List<EmoteCount>> response = userController.getTopEmotes(userId, 2);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void getTopEmotes_should_return_bottom_emotes_when_user_exists_limited_to_provided_count() {
        String userId = "1";
        Streamer streamer = new Streamer();

        when(userService.getById(userId)).thenReturn(Optional.of(streamer));
        when(activityService.getTopEmotes(streamer)).thenReturn(createEmountCountList(10, true));
        when(userService.getEmotesWithNoUsage(streamer)).thenReturn(Collections.emptyList());

        ResponseEntity<List<EmoteCount>> response = userController.getTopEmotes(userId, 2);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody())
            .isNotNull()
            .hasSize(2)
            .extracting(EmoteCount::getName, EmoteCount::getCount)
            .containsExactlyInAnyOrder(
                tuple("emote9", 10L),
                tuple("emote8", 9L));
    }


    @Test
    void getUser_should_return_user_when_user_exists() {
        String userId = "1";
        SimpleUser user = new SimpleUser();

        when(userService.getSimpleUser(userId)).thenReturn(Optional.of(user));

        ResponseEntity<SimpleUser> response = userController.getUser(userId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody())
            .isNotNull()
            .isEqualTo(user);
    }

    @Test
    void getUser_should_return_not_found_when_user_does_not_exist() {
        String userId = "1";

        when(userService.getSimpleUser(userId)).thenReturn(Optional.empty());

        ResponseEntity<SimpleUser> response = userController.getUser(userId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void registerNewUser_should_return_ok_when_new_user_is_registered() {
        String username = "username";
        Streamer streamer = new Streamer();

        when(userService.existsByUsername(username)).thenReturn(false);
        when(userService.create(username)).thenReturn(streamer);

        ResponseEntity<String> response = userController.registerNewUser(username);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void registerNewUser_should_return_not_modified_when_user_already_exists() {
        String username = "username";

        when(userService.existsByUsername(username)).thenReturn(true);

        ResponseEntity<String> response = userController.registerNewUser(username);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_MODIFIED);
    }

    @Test
    void registerNewUser_should_return_bad_request_when_illegal_state_exception_occurs() {
        String username = "username";

        when(userService.existsByUsername(username)).thenReturn(false);
        when(userService.create(username)).thenThrow(IllegalStateException.class);

        ResponseEntity<String> response = userController.registerNewUser(username);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void unregisterUser_should_return_ok_when_user_is_unregistered() {
        String username = "username";

        when(registrationService.unregister(username)).thenReturn(true);

        ResponseEntity<String> response = userController.unregisterUser(username);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void unregisterUser_should_return_not_modified_when_user_is_not_unregistered() {
        String username = "username";

        when(registrationService.unregister(username)).thenReturn(false);

        ResponseEntity<String> response = userController.unregisterUser(username);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_MODIFIED);
    }

    @Test
    void updateEmotesForUser_should_return_ok_when_user_exists() {
        String userId = "1";
        EmoteUpdateLog emoteUpdateLog = new EmoteUpdateLog();

        when(userService.existsById(userId)).thenReturn(true);
        when(userService.updateEmotes(userId)).thenReturn(emoteUpdateLog);

        ResponseEntity<EmoteUpdateLog> response = userController.updateEmotesForUser(userId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody())
            .isNotNull()
            .isEqualTo(emoteUpdateLog);
    }

    @Test
    void updateEmotesForUser_should_return_not_found_when_user_does_not_exist() {
        String userId = "1";

        when(userService.existsById(userId)).thenReturn(false);

        ResponseEntity<EmoteUpdateLog> response = userController.updateEmotesForUser(userId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

    }
}
