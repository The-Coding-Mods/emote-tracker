package de.jonas.emote.tracker.backend.twitch;


import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import de.jonas.emote.tracker.backend.configuration.OAuthConfiguration;
import org.springframework.stereotype.Component;

@Component
public class Client {
    private final TwitchClient twitchClient;
    private final MessageHandler messageHandler;

    public Client(OAuthConfiguration oauthConfig, MessageHandler messageHandler) {
        this.twitchClient = TwitchClientBuilder.builder().withEnableChat(true)
                .withEnableHelix(true).withChatAccount(oauthConfig.getCredential()).build();
        this.messageHandler = messageHandler;

        this.twitchClient.getEventManager().onEvent(ChannelMessageEvent.class, messageHandler);

    }

    public TwitchClient getTwitchClient() {
        return twitchClient;
    }

}
