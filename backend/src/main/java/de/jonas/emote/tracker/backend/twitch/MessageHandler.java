package de.jonas.emote.tracker.backend.twitch;

import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import de.jonas.emote.tracker.backend.model.database.EmoteCountMap;
import de.jonas.emote.tracker.backend.model.database.Streamer;
import de.jonas.emote.tracker.backend.user.UserRepository;
import jakarta.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MessageHandler implements Consumer<ChannelMessageEvent> {

    private final UserRepository userRepository;

    private final Map<String, Boolean> isPaused = new HashMap<>();

    public MessageHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public void accept(ChannelMessageEvent event) {
        if (isPaused.getOrDefault(event.getChannel().getId(), true)) {
            return;
        }
        final long startTime = System.currentTimeMillis();
        log.info("[{}] {}: {}", event.getChannel().getId(), event.getUser().getName(), event.getMessage());
        String message = event.getMessage();
        Streamer streamer = userRepository.getStreamerByTwitchUserId(event.getChannel().getId());

        List<String> matches =
            Pattern.compile(streamer.getEmoteRegex()).matcher(message).results().map(MatchResult::group).toList();
        log.debug("{}ms to complete regex", System.currentTimeMillis() - startTime);

        handleMatches(event.getUser().getName(), matches, streamer.getEmoteCounts());
        log.debug("{}ms to process matches", System.currentTimeMillis() - startTime);
        userRepository.saveAndFlush(streamer);
        log.info("Took {}ms to complete", System.currentTimeMillis() - startTime);
    }

    private static void handleMatches(String userName, List<String> matches, Set<EmoteCountMap> emoteCounts) {
        for (var match : matches) {
            final long startTime = System.currentTimeMillis();
            Optional<EmoteCountMap> optional = emoteCounts
                .stream()
                .filter(e -> e.getEmote().getName().equals(match))
                .findFirst();
            log.debug("{}ms to find match", System.currentTimeMillis() - startTime);

            if (optional.isPresent()) {
                optional.get().increaseCount();
                log.debug("{}ms to increase count", System.currentTimeMillis() - startTime);
                optional.get().getUniqueUsers().add(userName);
                log.debug("{}ms to insert unique user", System.currentTimeMillis() - startTime);
            }
        }
        log.debug("Emote count: {}", emoteCounts.toString());
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
