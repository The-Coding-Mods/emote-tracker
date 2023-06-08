package de.jonas.emote.tracker.backend.model.origin;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class User {
    @JsonProperty("id")
    private String id;
    @JsonProperty("display_name")
    private String displayName;
    @JsonProperty("emote_set")
    private EmoteSet emoteSet;
}
