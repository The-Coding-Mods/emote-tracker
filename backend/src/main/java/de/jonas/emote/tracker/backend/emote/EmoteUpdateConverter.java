package de.jonas.emote.tracker.backend.emote;

import de.jonas.emote.tracker.backend.api.model.EmoteUpdateLog;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

@Service
public class EmoteUpdateConverter implements Converter<EmoteUpdateDTO, EmoteUpdateLog> {

    private final EmoteConverter emoteConverter;

    public EmoteUpdateConverter(EmoteConverter emoteConverter) {
        this.emoteConverter = emoteConverter;
    }

    @Override
    public EmoteUpdateLog convert(EmoteUpdateDTO source) {
        return new EmoteUpdateLog()
            .added(source.added().stream().map(emoteConverter::convert).toList())
            .removed(source.removed().stream().map(emoteConverter::convert).toList())
            .renamed(source.updated().stream().map(emoteConverter::convert).toList());
    }
}
