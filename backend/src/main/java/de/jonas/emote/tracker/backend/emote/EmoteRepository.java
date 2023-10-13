package de.jonas.emote.tracker.backend.emote;

import de.jonas.emote.tracker.backend.model.database.Emote;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmoteRepository extends JpaRepository<Emote, String> {

    Optional<Emote> getEmoteByName(String name);
}
