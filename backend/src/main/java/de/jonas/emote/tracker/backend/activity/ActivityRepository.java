package de.jonas.emote.tracker.backend.activity;

import de.jonas.emote.tracker.backend.database.Activity;
import de.jonas.emote.tracker.backend.model.TimeRange;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivityRepository {
    List<Activity> getActivitiesByStreamerUsernameAndTimeStampAfterAndTimeStampBefore(String streamer,
                                                                                      Instant effFrom,
                                                                                      Instant effTil);

    default List<Activity> getActivitiesBetweenTimeRange(String streamer, TimeRange timeRange) {
        return getActivitiesByStreamerUsernameAndTimeStampAfterAndTimeStampBefore(streamer, timeRange.from(),
            timeRange.to());
    }
}
