package de.jonas.emote.tracker.backend.model.database;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Entity
@Getter
@Setter
@Accessors(chain = true)
public class Emote {

    @Id
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
