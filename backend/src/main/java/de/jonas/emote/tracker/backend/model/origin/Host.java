package de.jonas.emote.tracker.backend.model.origin;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

@Getter

public class Host {
    @JsonProperty("url")
    private String url;
    @JsonProperty("files")
    private List<File> files;
}
