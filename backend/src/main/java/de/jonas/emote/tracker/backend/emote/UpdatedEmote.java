package de.jonas.emote.tracker.backend.emote;

import de.jonas.emote.tracker.backend.database.Emote;
import lombok.Getter;

@Getter
public class UpdatedEmote extends Emote {
    private final String oldName;

    public UpdatedEmote(Emote emote, String oldName) {
        super.id = emote.getId();
        super.name = emote.getName();
        this.oldName = oldName;
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
