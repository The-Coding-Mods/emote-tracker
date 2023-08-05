package de.jonas.emote.tracker.backend.user;

import de.jonas.emote.tracker.backend.emote.SevenTVService;
import de.jonas.emote.tracker.backend.model.database.Emote;
import de.jonas.emote.tracker.backend.model.database.EmoteCountMap;
import de.jonas.emote.tracker.backend.model.database.User;
import de.jonas.emote.tracker.backend.twitch.MessageHandler;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Service;

@Service
public class EmoteUpdateService {
    private final SevenTVService sevenTVService;
    private final UserRepository userRepository;
    private final MessageHandler messageHandler;

    public EmoteUpdateService(SevenTVService sevenTVService, UserRepository userRepository,
                              MessageHandler messageHandler) {
        this.sevenTVService = sevenTVService;
        this.userRepository = userRepository;
        this.messageHandler = messageHandler;
    }

    public void updateUserEmotes(String userId) {
        messageHandler.pause(userId);
        User user = userRepository.getUserByTwitchUserId(userId);
        List<Emote> oldEmotes = user.getEmoteCounts()
            .stream()
            .filter(EmoteCountMap::isEnabled)
            .map(EmoteCountMap::getEmote)
            .toList();

        List<Emote> newEmotes = sevenTVService.getSevenTVEmotes(userId);

        List<Emote> removed = new ArrayList<>();
        List<Emote> added = new ArrayList<>();

        for (final var emote : oldEmotes) {
            if (!newEmotes.contains(emote)) {
                removed.add(emote);
            }
        }

        for (final var emote : newEmotes) {
            if (!oldEmotes.contains(emote)) {
                added.add(emote);
            }
        }

        Set<EmoteCountMap> emoteCountMap = user.getEmoteCounts();
        emoteCountMap.forEach(e -> {
            if (removed.contains(e.getEmote())) {
                e.setEnabled(false);
            }
        });
        emoteCountMap.addAll(EmoteCountMap.fromEmoteList(added, user));

        user.setEmoteCounts(emoteCountMap);
        user.setEmoteRegex(SevenTVService.buildRegexString(newEmotes));

        userRepository.saveAndFlush(user);
        messageHandler.start(userId);
    }
}
