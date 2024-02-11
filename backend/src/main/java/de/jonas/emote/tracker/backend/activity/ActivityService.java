package de.jonas.emote.tracker.backend.activity;

import de.jonas.emote.tracker.backend.api.model.EmoteCount;
import de.jonas.emote.tracker.backend.database.Activity;
import de.jonas.emote.tracker.backend.database.Emote;
import de.jonas.emote.tracker.backend.database.Streamer;
import java.time.Instant;
import java.util.List;
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

    public void createEmoteUpdateActivity(Streamer streamer) {
        Activity activity = new Activity()
            .setActivityType(Activity.Type.EMOTE_UPDATE)
            .setStreamer(streamer)
            .setTimeStamp(Instant.now());
        activityRepository.saveAndFlush(activity);
    }

    public List<Activity> getActivitiesByStreamer(Streamer streamer) {
        return activityRepository.getActivitiesByStreamer(streamer);
    }

    public List<EmoteCount> getTopEmotes(Streamer streamer) {
        return activityRepository.getEmoteUsageForStreamerDescending(streamer);
    }

    public List<EmoteCount> getBottomEmotes(Streamer streamer) {
        return activityRepository.getEmoteUsageForStreamerAscending(streamer);
    }

    public Instant getLastUpdate(Streamer streamer) {
        return activityRepository.getLastEmoteUpdate(streamer);
    }
}
