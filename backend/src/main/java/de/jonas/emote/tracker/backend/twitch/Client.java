package de.jonas.emote.tracker.backend.twitch;

import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import com.github.twitch4j.events.ChannelGoLiveEvent;
import de.jonas.emote.tracker.backend.configuration.OAuthConfiguration;
import org.springframework.stereotype.Component;

@Component
public class Client {
    private final TwitchClient twitchClient;

    public Client(OAuthConfiguration oauthConfig, MessageHandler messageHandler, LiveEventHandler liveEventHandler) {
        this.twitchClient = TwitchClientBuilder.builder()
            .withEnableChat(true)
            .withEnableHelix(true)
            .withChatAccount(oauthConfig.getCredential())
            .build();

        this.twitchClient.getEventManager().onEvent(ChannelMessageEvent.class, messageHandler);
        this.twitchClient.getEventManager().onEvent(ChannelGoLiveEvent.class, liveEventHandler);
    }

    public TwitchClient getTwitchClient() {
        return twitchClient;
    }
}
