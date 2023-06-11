package de.jonas.emote.tracker.backend.model.database;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.util.ArrayList;
import java.util.List;
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


    public static List<EmoteCountMap> fromEmoteList(List<Emote> emotes, User user) {
        List<EmoteCountMap> emoteCounts = new ArrayList<>();
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
}
