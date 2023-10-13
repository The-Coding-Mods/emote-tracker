package de.jonas.emote.tracker.backend.databasev2;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class CustomEmoteNameId implements Serializable {

    @Column(name = "emote_id")
    private String emoteId;
    @Column(name = "streamer_twitch_user_id")
    private String twitchUserId;

}
