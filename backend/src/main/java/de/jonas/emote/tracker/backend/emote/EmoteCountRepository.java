package de.jonas.emote.tracker.backend.emote;

import de.jonas.emote.tracker.backend.model.database.EmoteCountMap;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmoteCountRepository extends JpaRepository<EmoteCountMap, String> {

    List<EmoteCountMap> getEmoteCountMapsByUserTwitchUserId(String userId);

    default List<EmoteCountMap> getEmotesByUserId(String userId) {
        return getEmoteCountMapsByUserTwitchUserId(userId);
    }


    List<EmoteCountMap> getEmoteCountMapsByUserTwitchUserIdAndEnabledIsTrueOrderByCountDesc(String userId);

    default List<EmoteCountMap> getEnabledEmotesDescOrder(String userId) {
        return getEmoteCountMapsByUserTwitchUserIdAndEnabledIsTrueOrderByCountDesc(userId);
    }


    List<EmoteCountMap> getEmoteCountMapsByUserTwitchUserIdAndEnabledIsTrueOrderByCount(String userId);

    default List<EmoteCountMap> getEnabledEmotesOrder(String userId) {
        return getEmoteCountMapsByUserTwitchUserIdAndEnabledIsTrueOrderByCount(userId);
    }


    List<EmoteCountMap> getEmoteCountMapsByUserTwitchUserIdAndCountLessThanEqual(String userId, Integer count);

    default List<EmoteCountMap> getEmotesWithLessOrEqualCount(String userId, Integer count) {
        return getEmoteCountMapsByUserTwitchUserIdAndCountLessThanEqual(userId, count);
    }


}
