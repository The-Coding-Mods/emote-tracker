package de.jonas.emote.tracker.backend.model.database;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import java.util.Set;
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
    @Column(length = 65536)
    private String emoteRegex;


    @OneToMany(cascade = CascadeType.ALL)
    private Set<EmoteCountMap> emoteCounts;

    public void increaseEmoteCount(Emote emote) {
        emoteCounts
            .stream()
            .filter(emoteCountMap -> emoteCountMap.getEmote().equals(emote))
            .findFirst()
            .ifPresent(EmoteCountMap::increaseCount);
    }
}
