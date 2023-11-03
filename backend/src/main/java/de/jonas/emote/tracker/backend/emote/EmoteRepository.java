package de.jonas.emote.tracker.backend.emote;

import de.jonas.emote.tracker.backend.databasev2.UserEmote;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmoteRepository extends JpaRepository<UserEmote, String> {

    Optional<UserEmote> getUserEmoteByCustomEmoteName(String name);
}
