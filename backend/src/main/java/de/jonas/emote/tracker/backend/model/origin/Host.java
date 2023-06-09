package de.jonas.emote.tracker.backend.model.origin;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Getter;

@Getter
public class Host {
    @JsonProperty("url")
    private String url;

    @JsonProperty("files")
    private List<File> files;
}
