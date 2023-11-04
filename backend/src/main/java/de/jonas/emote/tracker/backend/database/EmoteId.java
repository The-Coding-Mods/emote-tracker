package de.jonas.emote.tracker.backend.database;

import java.io.Serializable;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmoteId implements Serializable {
    private String id;
    private String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EmoteId emoteId = (EmoteId) o;
        return Objects.equals(id, emoteId.id) && Objects.equals(name, emoteId.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
