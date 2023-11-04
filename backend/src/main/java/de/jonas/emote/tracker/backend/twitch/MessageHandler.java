package de.jonas.emote.tracker.backend.twitch;

import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import de.jonas.emote.tracker.backend.database.Activity;
import de.jonas.emote.tracker.backend.database.Emote;
import de.jonas.emote.tracker.backend.database.Streamer;
import de.jonas.emote.tracker.backend.emote.EmoteRepository;
import de.jonas.emote.tracker.backend.repository.ActivityRepository;
import de.jonas.emote.tracker.backend.user.UserRepository;
import jakarta.transaction.Transactional;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MessageHandler implements Consumer<ChannelMessageEvent> {

    private final UserRepository userRepository;
    private final EmoteRepository emoteRepository;
    private final ActivityRepository activityRepository;

    private final Map<String, Boolean> isPaused = new HashMap<>();

    public MessageHandler(UserRepository userRepository, EmoteRepository emoteRepository,
                          ActivityRepository activityRepository) {
        this.userRepository = userRepository;
        this.emoteRepository = emoteRepository;
        this.activityRepository = activityRepository;
    }

    @Override
    @Transactional
    public void accept(ChannelMessageEvent event) {
        if (Boolean.TRUE.equals(isPaused.getOrDefault(event.getChannel().getId(), true))) {
            return;
        }
        final long startTime = System.currentTimeMillis();
        log.info("[{}] {}: {}", event.getChannel().getId(), event.getUser().getName(), event.getMessage());
        String message = event.getMessage();
        Streamer streamer = userRepository.getStreamerByTwitchUserId(event.getChannel().getId());
        Set<String> emotes = streamer.getUserEmotes().stream().map(Emote::getName).collect(Collectors.toSet());
        List<String> splitMessage = List.of(message.split(" "));
        for (final var part : splitMessage) {
            if (emotes.contains(part)) {
                handleMatch(event.getUser().getName(), part, streamer);
            }
        }
        log.debug("{}ms to process matches", System.currentTimeMillis() - startTime);
        log.info("Took {}ms to complete", System.currentTimeMillis() - startTime);
    }

    private void handleMatch(String userName, String match, Streamer streamer) {
        Optional<Emote> emote = emoteRepository.getEmoteByName(match);
        if (emote.isEmpty()) {
            log.warn("A message part matches emote regex but no emote in database");
            return;
        }
        Activity activity = new Activity()
            .setActivityType(Activity.Type.EMOTE_USAGE)
            .setStreamer(streamer)
            .setEmote(emote.get())
            .setTimeStamp(Instant.now())
            .setUserName(userName);

        activityRepository.saveAndFlush(activity);
    }

    public void pause(String userId) {
        log.info("Pause MessageHandler for {}", userId);
        this.isPaused.put(userId, true);
    }

    public void start(String userId) {
        log.info("Start MessageHandler for {}", userId);
        this.isPaused.put(userId, false);
    }
}
