package de.jonas.emote.tracker.backend.repository;

import de.jonas.emote.tracker.backend.model.database.User;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

}
