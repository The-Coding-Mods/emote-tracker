package de.jonas.emote.tracker.backend.user;

import de.jonas.emote.tracker.backend.api.model.SimpleUser;
import de.jonas.emote.tracker.backend.database.Streamer;
import org.springframework.stereotype.Service;

@Service
public class UserConverter {
    SimpleUser toSimpleUser(Streamer streamer) {
        return new SimpleUser()
            .name(streamer.getUsername())
            .id(streamer.getTwitchUserId());
    }
}
