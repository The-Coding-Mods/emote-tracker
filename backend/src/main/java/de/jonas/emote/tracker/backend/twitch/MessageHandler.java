package de.jonas.emote.tracker.backend.twitch;

import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import de.jonas.emote.tracker.backend.activity.ActivityService;
import de.jonas.emote.tracker.backend.database.Emote;
import de.jonas.emote.tracker.backend.database.Streamer;
import de.jonas.emote.tracker.backend.user.UserRepository;
import jakarta.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.VisibleForTesting;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MessageHandler implements Consumer<ChannelMessageEvent> {

    private final UserRepository userRepository;
    private final ActivityService activityService;
    private final Map<String, Boolean> isPaused = new HashMap<>();

    public MessageHandler(UserRepository userRepository, ActivityService activityService) {
        this.userRepository = userRepository;
        this.activityService = activityService;
    }

    @Override
    @Transactional
    public void accept(ChannelMessageEvent event) {
        if (Boolean.TRUE.equals(isPaused.getOrDefault(event.getChannel().getId(), true))) {
            return;
        }
        final long startTime = System.currentTimeMillis();
        String message = event.getMessage();
        Streamer streamer = userRepository.getStreamerByTwitchUserId(event.getChannel().getId());
        Map<String, Emote> emotes = streamer.getUserEmotes().stream().collect(Collectors.toMap(Emote::getName, e -> e));
        List<String> splitMessage = List.of(message.split(" "));
        for (final var part : splitMessage) {
            if (emotes.containsKey(part)) {
                activityService.createEmoteUsageActivity(streamer, emotes.get(part), event.getUser().getName());
            }
        }
        log.debug("Took {}ms to complete", System.currentTimeMillis() - startTime);
    }

    public void pause(String userId) {
        log.info("Pause MessageHandler for {}", userId);
        this.isPaused.put(userId, true);
    }

    public void start(String userId) {
        log.info("Start MessageHandler for {}", userId);
        this.isPaused.put(userId, false);
    }

    @VisibleForTesting
    Map<String, Boolean> getIsPaused() {
        return isPaused;
    }
}
