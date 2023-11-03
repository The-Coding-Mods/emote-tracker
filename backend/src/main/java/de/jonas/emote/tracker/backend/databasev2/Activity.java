package de.jonas.emote.tracker.backend.databasev2;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import java.time.Instant;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Entity
@Getter
@Setter
@Accessors(chain = true)
public class Activity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private ActivityType activityType;

    private String userName;

    @ManyToOne(cascade = CascadeType.ALL)
    private Emote emote;

    @ManyToOne(cascade = CascadeType.ALL)
    private Streamer streamer;

    private Instant timeStamp;
}
