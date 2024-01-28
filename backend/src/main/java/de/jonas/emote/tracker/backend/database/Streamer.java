package de.jonas.emote.tracker.backend.database;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import java.time.Instant;
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
    private String username;

    @ManyToMany(cascade = CascadeType.ALL)
    private Set<Emote> userEmotes;

    private Instant registered;

    private String profilePictureUrl;

}
