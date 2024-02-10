package de.jonas.emote.tracker.backend.user;

import de.jonas.emote.tracker.backend.activity.ActivityService;
import de.jonas.emote.tracker.backend.api.model.SimpleUser;
import de.jonas.emote.tracker.backend.database.Streamer;
import org.springframework.stereotype.Service;

@Service
public class UserConverter {
    private final ActivityService activityService;

    public UserConverter(ActivityService activityService) {
        this.activityService = activityService;
    }

    SimpleUser toSimpleUser(Streamer streamer) {
        return new SimpleUser()
            .name(streamer.getUsername())
            .id(streamer.getTwitchUserId())
            .profilePicture(streamer.getProfilePictureUrl())
            .registered(streamer.getRegistered())
            .lastUpdated(activityService.getLastUpdate(streamer));
    }
}
