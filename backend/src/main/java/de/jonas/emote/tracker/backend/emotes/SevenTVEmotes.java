package de.jonas.emote.tracker.backend.emotes;

import de.jonas.emote.tracker.backend.model.database.Emote;
import de.jonas.emote.tracker.backend.model.database.User;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SevenTVEmotes {

    private Map<String, User> userMap = new HashMap<>();

    public void insertUser(User user) {
        userMap.put(user.getTwitchUserId(), user);
    }

    public void increaseEmoteForUser(String userId, Emote emote) {
        userMap.get(userId).increaseEmoteCount(emote);
    }

    public boolean contains(String userId, Emote emote) {
        return userMap.get(userId).getEmoteCounts().containsKey(emote);
    }

    public void printDebugForUser(String userId) {
        log.info("User: {}\n{}", userId, userMap.get(userId).getEmoteCounts().toString());
    }
}
