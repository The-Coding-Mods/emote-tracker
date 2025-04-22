package de.jonas.emote.tracker.backend.database;

import jakarta.persistence.*;

import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Entity
@Getter
@Setter
@Accessors(chain = true)
@IdClass(EmoteId.class)
@Table(indexes = {@Index(name = "idx_emote_id", columnList = "id"), @Index(name = "idx_emote_name", columnList = "name")})
public class Emote {
    @Id
    protected String id;
    @Id
    protected String name;
    protected Source source;

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
        return Objects.hash(id, name);
    }
}
