package de.jonas.emote.tracker.backend.user;

import de.jonas.emote.tracker.backend.database.Streamer;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<Streamer, String> {
    Streamer getStreamerByTwitchUserId(String userId);

    Boolean existsStreamerByUsername(String username);

}
