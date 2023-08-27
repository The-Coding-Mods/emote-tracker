package de.jonas.emote.tracker.backend.twitch;

import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import com.github.twitch4j.events.ChannelGoLiveEvent;
import de.jonas.emote.tracker.backend.configuration.OAuthConfiguration;
import jakarta.annotation.PreDestroy;
import lombok.Getter;
import org.springframework.stereotype.Component;

@Getter
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

    public boolean leaveChannel(String username) {
        return this.twitchClient.getChat().leaveChannel(username);
    }

    @PreDestroy
    public void destroy() {
        for (String channel : this.twitchClient.getChat().getChannels()) {
            this.twitchClient.getChat().leaveChannel(channel);
        }
        this.twitchClient.getChat().disconnect();
    }

    public boolean isChannelJoined(String username) {
        return this.twitchClient.getChat().isChannelJoined(username);
    }

    public void joinChannel(String username) {
        this.twitchClient.getChat().joinChannel(username);
    }
}
