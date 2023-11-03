package de.jonas.emote.tracker.backend.model;

import java.time.Instant;

public record TimeRange(Instant to, Instant from) {
    static TimeRange ALL = new TimeRange(Instant.MIN, Instant.MAX);

    boolean intersects(TimeRange other) {
        return this.to.isAfter(other.from) && this.from.isBefore(other.to);
    }
}
