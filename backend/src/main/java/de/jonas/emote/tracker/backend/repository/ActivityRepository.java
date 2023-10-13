package de.jonas.emote.tracker.backend.repository;

import de.jonas.emote.tracker.backend.databasev2.Activity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivityRepository extends JpaRepository<Activity, UUID> {
}
