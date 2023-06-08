package de.jonas.emote.tracker.backend.model.origin;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

@Getter
public class EmoteSet {
    @JsonProperty("id")
    private String id;

    @JsonProperty("emotes")
    private List<Emote> emotes;
}
