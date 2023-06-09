package de.jonas.emote.tracker.backend.model.database;

import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Emote {
    private String id;
    private String name;
    private Source source;

    @Override
    public String toString() {
        return "[" + id + ", " + name + ", " + source + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Emote emote = (Emote) o;
        return Objects.equals(name, emote.name) && source == emote.source;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, source);
    }
}
