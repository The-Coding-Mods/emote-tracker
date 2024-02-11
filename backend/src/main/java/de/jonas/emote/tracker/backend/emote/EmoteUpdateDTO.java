package de.jonas.emote.tracker.backend.emote;

import de.jonas.emote.tracker.backend.database.Emote;
import java.util.Set;

public record EmoteUpdateDTO(Set<Emote> added, Set<Emote> removed, Set<UpdatedEmote> updated) {
}
