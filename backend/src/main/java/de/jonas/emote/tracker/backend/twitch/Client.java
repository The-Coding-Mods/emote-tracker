package de.jonas.emote.tracker.backend.twitch;


import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Client {
    private TwitchClient twitchClient;

    @Value("${access.token}")
    private String accessToken;

    @PostConstruct
    public void construct() {
        OAuth2Credential credential = new OAuth2Credential("twitch", accessToken);
        twitchClient = TwitchClientBuilder.builder().withEnableChat(true)
                .withEnableHelix(true).withChatAccount(credential).build();
    }

    public TwitchClient getTwitchClient() {
        return twitchClient;
    }

}
