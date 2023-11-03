package de.jonas.emote.tracker.backend.databasev2;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import java.util.Objects;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Cascade;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@Entity
public class UserEmote extends Emote {

    public UserEmote(String customEmoteName, String id, String originalName, Source source) {
        super(id, originalName, source);
        this.customEmoteName = customEmoteName;
    }

    String customEmoteName;


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        UserEmote userEmote = (UserEmote) o;
        return Objects.equals(customEmoteName, userEmote.customEmoteName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), customEmoteName);
    }
}
