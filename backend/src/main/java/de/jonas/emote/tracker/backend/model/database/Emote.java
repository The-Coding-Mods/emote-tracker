package de.jonas.emote.tracker.backend.model.database;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import java.util.Objects;
import java.util.Set;
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

    @ElementCollection(targetClass = String.class, fetch = FetchType.LAZY)
    @CollectionTable(name = "unique_users", joinColumns = @JoinColumn(name = "emote_id"))
    private Set<String> uniqueUsers;

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
