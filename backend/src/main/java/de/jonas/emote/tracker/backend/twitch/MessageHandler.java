package de.jonas.emote.tracker.backend.twitch;

import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import de.jonas.emote.tracker.backend.model.database.EmoteCountMap;
import de.jonas.emote.tracker.backend.model.database.User;
import de.jonas.emote.tracker.backend.repository.EmoteCountRepository;
import de.jonas.emote.tracker.backend.user.UserRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.function.Consumer;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MessageHandler implements Consumer<ChannelMessageEvent> {

    private final EmoteCountRepository countRepository;
    private final UserRepository userRepository;

    public MessageHandler(EmoteCountRepository countRepository, UserRepository userRepository) {
        this.countRepository = countRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public void accept(ChannelMessageEvent event) {
        final long startTime = System.currentTimeMillis();
        String userId = event.getChannel().getId();
        String message = event.getMessage();
        log.info(
            "[{}] {}: {}", event.getChannel().getId(), event.getUser().getName(), event.getMessage());
        User dbUser = userRepository.getUserByTwitchUserId(userId);
        List<String> matches =
            Pattern.compile(dbUser.getEmoteRegex()).matcher(message).results().map(MatchResult::group).toList();
        List<EmoteCountMap> emoteCounts =
            countRepository.getEmoteCountMapsByUserTwitchUserId(event.getChannel().getId());
        for (var match : matches) {
            emoteCounts
                .stream()
                .filter(e -> e.getEmote().getName().equals(match))
                .findFirst()
                .ifPresent(EmoteCountMap::increaseCount);
        }
        log.debug("Emote count: {}", emoteCounts.toString());
        countRepository.saveAllAndFlush(emoteCounts);
        log.info("Took {} ms to process message", System.currentTimeMillis() - startTime);
    }
}
