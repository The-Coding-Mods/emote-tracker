package de.jonas.emote.tracker.backend.user;

import de.jonas.emote.tracker.backend.api.model.Emote;
import java.util.Collections;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    public List<Emote> getTopEmotes(String userId, Integer count) {
        return Collections.emptyList();
    }

    public List<Emote> getBottomEmotes(String userId, Integer count) {
        return Collections.emptyList();
    }

    public List<Emote> getEmotesWithNrUsage(String userId, Integer count) {
        return Collections.emptyList();
    }

    public List<Emote> getEmotesAboveAverage(String userId) {
        return Collections.emptyList();
    }
    public List<Emote> getEmotesBelowAverage(String userId) {
        return Collections.emptyList();

    }
}
