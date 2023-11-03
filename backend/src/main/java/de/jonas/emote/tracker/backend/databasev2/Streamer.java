package de.jonas.emote.tracker.backend.databasev2;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Entity
@Accessors(chain = true)
public class Streamer {
    @Id
    private String twitchUserId;
    private String sevenTVUserId;
    private String username;


    @OneToMany(cascade = CascadeType.ALL)
    private Set<UserEmote> userEmotes;

}
