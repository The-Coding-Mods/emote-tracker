package de.jonas.emote.tracker.backend.configuration;

import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OAuthConfiguration {
    private final OAuth2Credential credential;

    public OAuthConfiguration(@Value("${access.token}") String accessToken) {
        this.credential = new OAuth2Credential("twitch", accessToken);
    }

    public OAuth2Credential getCredential() {
        return credential;
    }
}
