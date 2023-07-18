package de.jonas.emote.tracker.backend.user;

import de.jonas.emote.tracker.backend.api.model.Emote;
import de.jonas.emote.tracker.backend.model.database.EmoteCountMap;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

@Service
public class EmoteCountConverter implements Converter<EmoteCountMap, Emote> {
    @Override
    public Emote convert(EmoteCountMap source) {
        return new Emote().count(source.getCount()).id(source.getEmote().getId());
    }
}
