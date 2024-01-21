package de.jonas.emote.tracker.backend.activity;

import de.jonas.emote.tracker.backend.database.Activity;
import de.jonas.emote.tracker.backend.database.Emote;
import de.jonas.emote.tracker.backend.database.Streamer;
import java.time.Instant;
import org.springframework.stereotype.Service;

@Service
public class ActivityService {

    private final ActivityRepository activityRepository;

    public ActivityService(ActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    public void createEmoteUsageActivity(Streamer streamer, Emote emote, String userName) {
        Activity activity = new Activity()
            .setActivityType(Activity.Type.EMOTE_USAGE)
            .setStreamer(streamer)
            .setEmote(emote)
            .setTimeStamp(Instant.now())
            .setUserName(userName);
        activityRepository.saveAndFlush(activity);
    }
}
