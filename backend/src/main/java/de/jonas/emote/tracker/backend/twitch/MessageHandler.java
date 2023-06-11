package de.jonas.emote.tracker.backend.twitch;

import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import de.jonas.emote.tracker.backend.model.database.Emote;
import de.jonas.emote.tracker.backend.model.database.EmoteCountMap;
import de.jonas.emote.tracker.backend.model.database.Source;
import de.jonas.emote.tracker.backend.repository.EmoteCountRepository;
import java.util.List;
import java.util.function.Consumer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MessageHandler implements Consumer<ChannelMessageEvent> {

    private final EmoteCountRepository countRepository;

    public MessageHandler(EmoteCountRepository countRepository) {
        this.countRepository = countRepository;
    }

    @Override
    public void accept(ChannelMessageEvent event) {
        final long startTime = System.currentTimeMillis();
        log.info(
            "[{}] {}: {}", event.getChannel().getId(), event.getUser().getName(), event.getMessage());
        List<EmoteCountMap> emoteCounts =
            countRepository.getEmoteCountMapsByUserTwitchUserId(event.getChannel().getId());
        for (var str : event.getMessage().split(" ")) {
            Emote asEmote = new Emote().setName(str).setSource(Source.SEVENTV);
            if (emoteCounts.stream().anyMatch(e -> e.getEmote().equals(asEmote))) {
                emoteCounts.stream().filter(e -> e.getEmote().equals(asEmote)).findFirst()
                    .ifPresent(EmoteCountMap::increaseCount);
            }
        }
        log.debug("Emote count: {}", emoteCounts.toString());
        countRepository.saveAllAndFlush(emoteCounts);
        log.info("Took {} ms to process message", System.currentTimeMillis() - startTime);
    }
}
