package de.jonas.emote.tracker.backend.emote;

import de.jonas.emote.tracker.backend.model.database.EmoteCountMap;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EmoteCountRepository extends JpaRepository<EmoteCountMap, String> {


    List<EmoteCountMap> getEmoteCountMapsByStreamerTwitchUserId(String userId);

    default List<EmoteCountMap> getEmotesByUserId(String userId) {
        return getEmoteCountMapsByStreamerTwitchUserId(userId);
    }

    List<EmoteCountMap> getEmoteCountMapsByStreamerTwitchUserIdAndEnabledIsTrueOrderByCountDesc(String userId);

    default List<EmoteCountMap> getEnabledEmotesDescOrder(String userId) {
        return getEmoteCountMapsByStreamerTwitchUserIdAndEnabledIsTrueOrderByCountDesc(userId);
    }


    List<EmoteCountMap> getEmoteCountMapsByStreamerTwitchUserIdAndEnabledIsTrueOrderByCount(String userId);

    default List<EmoteCountMap> getEnabledEmotesOrder(String userId) {
        return getEmoteCountMapsByStreamerTwitchUserIdAndEnabledIsTrueOrderByCount(userId);
    }


    List<EmoteCountMap> getEmoteCountMapsByStreamerTwitchUserIdAndCountLessThanEqual(String userId, Integer count);

    default List<EmoteCountMap> getEmotesWithLessOrEqualCount(String userId, Integer count) {
        return getEmoteCountMapsByStreamerTwitchUserIdAndCountLessThanEqual(userId, count);
    }

    @Query("SELECT e FROM EmoteCountMap e WHERE e.streamer.twitchUserId = ?1 AND"
        + " e.count < (SELECT AVG(e.count) FROM EmoteCountMap e WHERE e.streamer.twitchUserId = ?1)")
    List<EmoteCountMap> getEmotesBelowAverage(String userId);

    @Query("SELECT e FROM EmoteCountMap e WHERE e.streamer.twitchUserId = ?1 AND"
        + " e.count >= (SELECT AVG(e.count) FROM EmoteCountMap e WHERE e.streamer.twitchUserId = ?1)")
    List<EmoteCountMap> getEmotesAboveAverage(String userId);
}
