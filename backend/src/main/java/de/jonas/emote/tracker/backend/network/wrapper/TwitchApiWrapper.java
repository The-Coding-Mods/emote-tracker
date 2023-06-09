package de.jonas.emote.tracker.backend.network.wrapper;

import com.github.twitch4j.helix.TwitchHelix;
import com.github.twitch4j.helix.domain.UserList;
import de.jonas.emote.tracker.backend.configuration.OAuthConfiguration;
import de.jonas.emote.tracker.backend.twitch.Client;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class TwitchApiWrapper {
    private final OAuthConfiguration oauthConfiguration;
    private final TwitchHelix twitchApi;

    public TwitchApiWrapper(OAuthConfiguration oauthConfiguration, Client client) {
        this.oauthConfiguration = oauthConfiguration;
        this.twitchApi = client.getTwitchClient().getHelix();
    }

    public UserList getUsers(List<String> userIDs, List<String> userNames) {
        return this.twitchApi
            .getUsers(oauthConfiguration.getCredential().getAccessToken(), userIDs, userNames)
            .execute();
    }
}
