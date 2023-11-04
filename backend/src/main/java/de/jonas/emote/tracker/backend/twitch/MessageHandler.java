package de.jonas.emote.tracker.backend.twitch;

import static de.jonas.emote.tracker.backend.emote.SevenTVService.buildRegexString;

import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import de.jonas.emote.tracker.backend.database.Activity;
import de.jonas.emote.tracker.backend.database.Emote;
import de.jonas.emote.tracker.backend.database.Streamer;
import de.jonas.emote.tracker.backend.emote.EmoteRepository;
import de.jonas.emote.tracker.backend.repository.ActivityRepository;
import de.jonas.emote.tracker.backend.user.UserRepository;
import jakarta.transaction.Transactional;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.regex.Pattern;
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

        List<String> splitMessage = List.of(message.split(" "));
        Pattern pattern = Pattern.compile(buildRegexString(streamer.getUserEmotes()));
        List<String> matches = new ArrayList<>();
        for (final var part : splitMessage) {
            if (pattern.matcher(part).matches()) {
                matches.add(part);
            }
        }
        log.debug("{}ms to complete regex", System.currentTimeMillis() - startTime);

        handleMatches(event.getUser().getName(), matches, streamer);
        log.debug("{}ms to process matches", System.currentTimeMillis() - startTime);
        log.info("Took {}ms to complete", System.currentTimeMillis() - startTime);
    }

    private void handleMatches(String userName, List<String> matches, Streamer streamer) {
        for (var match : matches) {
            Optional<Emote> emote = emoteRepository.getEmoteByName(match);
            if (emote.isEmpty()) {
                log.warn("A message part matches emote regex but no emote in database");
                continue;
            }
            Activity activity = new Activity()
                .setActivityType(Activity.Type.EMOTE_USAGE)
                .setStreamer(streamer)
                .setEmote(emote.get())
                .setTimeStamp(Instant.now())
                .setUserName(userName);

            activityRepository.saveAndFlush(activity);
        }
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
