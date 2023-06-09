package de.jonas.emote.tracker.backend.twitch;

import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import de.jonas.emote.tracker.backend.emotes.SevenTVEmotes;
import de.jonas.emote.tracker.backend.model.database.Emote;
import de.jonas.emote.tracker.backend.model.database.Source;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Slf4j
@Component
public class MessageHandler implements Consumer<ChannelMessageEvent> {
    private final SevenTVEmotes sevenTVEmotes;

    public MessageHandler(SevenTVEmotes sevenTVEmotes) {
        this.sevenTVEmotes = sevenTVEmotes;
    }

    @Override
    public void accept(ChannelMessageEvent event) {
        log.info("[{}] {}: {}", event.getChannel().getId(), event.getUser().getName(), event.getMessage());
        for (var str : event.getMessage().split(" ")) {
            Emote asEmote = new Emote(null, str, Source.SEVENTV);
            if (sevenTVEmotes.contains(event.getChannel().getId(), asEmote)) {
                sevenTVEmotes.increaseEmoteForUser(event.getChannel().getId(), asEmote);
                log.info("Emote count:");
                sevenTVEmotes.printDebugForUser(event.getChannel().getId());
            }
        }
    }
}
