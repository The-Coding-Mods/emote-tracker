package de.jonas.emote.tracker.backend.emote;

import de.jonas.emote.tracker.backend.database.Emote;
import de.jonas.emote.tracker.backend.database.EmoteId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmoteRepository extends JpaRepository<Emote, EmoteId> {

}
