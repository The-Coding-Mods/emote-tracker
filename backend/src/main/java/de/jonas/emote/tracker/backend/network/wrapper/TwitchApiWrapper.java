package de.jonas.emote.tracker.backend.network.wrapper;

import com.github.twitch4j.helix.TwitchHelix;
import com.github.twitch4j.helix.domain.UserList;
import de.jonas.emote.tracker.backend.configuration.OAuthConfiguration;
import de.jonas.emote.tracker.backend.twitch.Client;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TwitchApiWrapper {
    private final OAuthConfiguration oAuthConfiguration;
    private final TwitchHelix twitchApi;

    public TwitchApiWrapper(OAuthConfiguration oAuthConfiguration, Client client) {
        this.oAuthConfiguration = oAuthConfiguration;
        this.twitchApi = client.getTwitchClient().getHelix();
    }

    public UserList getUsers(List<String> userIDs, List<String> userNames) {
        return this.twitchApi.getUsers(oAuthConfiguration.getCredential().getAccessToken(), userIDs, userNames).execute();
    }
}
