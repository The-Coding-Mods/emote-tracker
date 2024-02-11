package de.jonas.emote.tracker.backend.emote;

import de.jonas.emote.tracker.backend.api.model.EmoteUpdate;
import de.jonas.emote.tracker.backend.database.Emote;
import org.springframework.stereotype.Service;

@Service
public class EmoteConverter {

    public de.jonas.emote.tracker.backend.api.model.Emote convert(Emote source) {
        return new de.jonas.emote.tracker.backend.api.model.Emote()
            .id(source.getId())
            .name(source.getName());
    }

    public EmoteUpdate convert(UpdatedEmote source) {
        return new EmoteUpdate()
            .id(source.getId())
            .name(source.getName())
            .oldName(source.getOldName());
    }
}
