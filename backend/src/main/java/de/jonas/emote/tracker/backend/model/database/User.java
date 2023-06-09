package de.jonas.emote.tracker.backend.model.database;

import lombok.Getter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class User {
    private final String twitchUserId;
    private final String username;
    private final String sevenTVUserId;
    private final Map<Emote, Integer> emoteCounts = new HashMap<>();

    public User(String twitchUserId, String username, String sevenTVUserId, List<Emote> emotes) {
        this.twitchUserId = twitchUserId;
        this.username = username;
        this.sevenTVUserId = sevenTVUserId;
        emotes.forEach(e -> emoteCounts.put(e, 0));
    }

    public void increaseEmoteCount(Emote emote) {
        emoteCounts.merge(emote, 1, Integer::sum);
    }
}

