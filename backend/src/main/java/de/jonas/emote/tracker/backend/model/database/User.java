package de.jonas.emote.tracker.backend.model.database;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Entity(name = "my_user")
@Accessors(chain = true)
public class User {
    @Id
    private String twitchUserId;
    private String sevenTVUserId;
    private String username;

    @OneToMany(cascade = CascadeType.ALL)
    private List<EmoteCountMap> emoteCounts;

    public void increaseEmoteCount(Emote emote) {
        emoteCounts
            .stream()
            .filter(emoteCountMap -> emoteCountMap.getEmote().equals(emote))
            .findFirst()
            .ifPresent(EmoteCountMap::increaseCount);
    }
}
