package de.jonas.emote.tracker.backend.twitch;

import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Slf4j
@Component
public class MessageHandler implements Consumer<ChannelMessageEvent> {

    @Override
    public void accept(ChannelMessageEvent event) {
        log.info("[{}] {}: {}", event.getChannel().getId(), event.getUser().getName(), event.getMessage());
        //event.reply(event.getTwitchChat(), "Hello world");
    }
}
