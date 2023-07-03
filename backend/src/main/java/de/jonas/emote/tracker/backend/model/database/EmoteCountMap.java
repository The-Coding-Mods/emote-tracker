package de.jonas.emote.tracker.backend.model.database;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Entity
@Getter
@Setter
@Accessors(chain = true)
public class EmoteCountMap {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @ManyToOne(cascade = CascadeType.ALL)
    private Emote emote;
    private Integer count;
    @ManyToOne(cascade = CascadeType.ALL)
    private User user;


    public static Set<EmoteCountMap> fromEmoteList(List<Emote> emotes, User user) {
        Set<EmoteCountMap> emoteCounts = new HashSet<>();
        emotes.forEach(e -> emoteCounts.add(new EmoteCountMap().setEmote(e).setUser(user).setCount(0)));
        return emoteCounts;
    }

    public void increaseCount() {
        count++;
    }

    @Override
    public String toString() {
        return "[" + emote + ": " + count + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EmoteCountMap that = (EmoteCountMap) o;
        return Objects.equals(emote, that.emote) && Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(emote, user);
    }
}
