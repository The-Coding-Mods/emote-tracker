package de.jonas.emote.tracker.backend.activity;

import de.jonas.emote.tracker.backend.api.model.EmoteCount;
import de.jonas.emote.tracker.backend.database.Activity;
import de.jonas.emote.tracker.backend.database.Streamer;
import de.jonas.emote.tracker.backend.model.TimeRange;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ActivityRepository extends JpaRepository<Activity, UUID> {

    List<Activity> getActivitiesByStreamer(Streamer streamer);

    @Query("""
        SELECT new de.jonas.emote.tracker.backend.api.model.EmoteCount(a.emote.id, a.emote.name, count(*))
        FROM Activity a WHERE a.streamer = ?1 AND a.activityType = 0 GROUP BY a.emote ORDER BY count(*) DESC""")
    List<EmoteCount> getEmoteUsageForStreamerDescending(Streamer streamer);

    @Query("""
        SELECT new de.jonas.emote.tracker.backend.api.model.EmoteCount(a.emote.id, a.emote.name, count(*))
        FROM Activity a WHERE a.streamer = ?1 AND a.activityType = 0 GROUP BY a.emote ORDER BY count(*) ASC""")
    List<EmoteCount> getEmoteUsageForStreamerAscending(Streamer streamer);

    @Query("""
        SELECT max(a.timeStamp) FROM Activity a WHERE a.streamer = ?1 AND a.activityType = 1
        """)
    Instant getLastEmoteUpdate(Streamer streamer);

    List<Activity> getActivitiesByStreamerUsernameAndTimeStampAfterAndTimeStampBefore(String streamer,
                                                                                      Instant effFrom,
                                                                                      Instant effTil);

    default List<Activity> getActivitiesBetweenTimeRange(String streamer, TimeRange timeRange) {
        return getActivitiesByStreamerUsernameAndTimeStampAfterAndTimeStampBefore(streamer, timeRange.from(),
            timeRange.to());
    }
}
