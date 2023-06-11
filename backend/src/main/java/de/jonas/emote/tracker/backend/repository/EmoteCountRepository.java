package de.jonas.emote.tracker.backend.repository;

import de.jonas.emote.tracker.backend.model.database.EmoteCountMap;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmoteCountRepository extends JpaRepository<EmoteCountMap, String> {
    List<EmoteCountMap> getEmoteCountMapsByUserTwitchUserId(String userId);
}
