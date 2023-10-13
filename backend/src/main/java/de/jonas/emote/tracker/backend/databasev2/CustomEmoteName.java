package de.jonas.emote.tracker.backend.databasev2;

import de.jonas.emote.tracker.backend.model.database.Emote;
import de.jonas.emote.tracker.backend.model.database.Streamer;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Entity
@Getter
@Setter
@Accessors(chain = true)
public class CustomEmoteName {
    @EmbeddedId
    private CustomEmoteNameId id;

    @OneToOne
    @MapsId("emote_id")
    private Emote emote;

    @OneToOne
    @MapsId("streamer_twitch_user_id")
    private Streamer streamer;

    private String customEmoteName;
}
