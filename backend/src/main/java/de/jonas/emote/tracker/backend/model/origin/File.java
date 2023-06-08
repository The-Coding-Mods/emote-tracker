package de.jonas.emote.tracker.backend.model.origin;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter

public class File {
    @JsonProperty("name")
    private String name;
    @JsonProperty("format")
    private String format;
}
