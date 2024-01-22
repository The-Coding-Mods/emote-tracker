package de.jonas.emote.tracker.backend.user;

import de.jonas.emote.tracker.backend.database.Emote;
import de.jonas.emote.tracker.backend.database.Streamer;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<Streamer, String> {
    Streamer getStreamerByTwitchUserId(String userId);

    Boolean existsStreamerByUsername(String username);

    @Query("""
        SELECT e FROM Streamer s JOIN s.userEmotes e
        WHERE s = ?1 AND NOT EXISTS (SELECT a FROM Activity a WHERE a.emote.id = e.id)""")
    List<Emote> getEmotesWithNoUsageForStreamer(Streamer streamer);
}
