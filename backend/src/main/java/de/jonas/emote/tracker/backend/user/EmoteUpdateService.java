package de.jonas.emote.tracker.backend.user;

import de.jonas.emote.tracker.backend.emote.SevenTVService;
import de.jonas.emote.tracker.backend.model.database.Emote;
import de.jonas.emote.tracker.backend.model.database.EmoteCountMap;
import de.jonas.emote.tracker.backend.model.database.Streamer;
import de.jonas.emote.tracker.backend.twitch.MessageHandler;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
        Streamer user = userRepository.getStreamerByTwitchUserId(userId);
        List<Emote> oldEmotes = user.getEmoteCounts()
            .stream()
            .filter(EmoteCountMap::isEnabled)
            .map(EmoteCountMap::getEmote)
            .toList();

        List<Emote> newEmotes = sevenTVService.getSevenTVEmotes(userId);

        List<Emote> removed = getRemovedEmotes(oldEmotes, newEmotes);
        List<Emote> added = getAddedEmotes(oldEmotes, newEmotes);

        removeRenamedEmotes(removed, added, handleRenamedEmotes(oldEmotes, removed, added));

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

    private static void removeRenamedEmotes(List<Emote> removed, List<Emote> added, List<Emote> renamed) {
        added.removeIf(e -> {
            for (final var renamedEmote : renamed) {
                if (e.getId().equals(renamedEmote.getId())) {
                    return true;
                }
            }
            return false;
        });
        removed.removeIf(e -> {
            for (final var renamedEmote : renamed) {
                if (e.getId().equals(renamedEmote.getId())) {
                    return true;
                }
            }
            return false;
        });
    }

    private static List<Emote> handleRenamedEmotes(List<Emote> oldEmotes, List<Emote> removed, List<Emote> added) {
        List<Emote> renamed = new ArrayList<>();
        for (final var addedEmote : added) {
            for (final var removedEmote : removed) {
                if (addedEmote.getId().equals(removedEmote.getId())) {
                    Optional<Emote> toRename = oldEmotes.stream().filter(e -> e.equals(removedEmote)).findFirst();
                    if (toRename.isPresent()) {
                        // TODO this updates the emote name globally and not user specific, two users can have different names for an emote.
                        toRename.get().setName(addedEmote.getName());
                        renamed.add(toRename.get());
                    }
                }
            }
        }
        return renamed;
    }

    private static List<Emote> getRemovedEmotes(List<Emote> oldEmotes, List<Emote> newEmotes) {
        List<Emote> removed = new ArrayList<>();
        for (final var emote : oldEmotes) {
            if (!newEmotes.contains(emote)) {
                removed.add(emote);
            }
        }
        return removed;
    }

    private static List<Emote> getAddedEmotes(List<Emote> oldEmotes, List<Emote> newEmotes) {
        List<Emote> added = new ArrayList<>();
        for (final var emote : newEmotes) {
            if (!oldEmotes.contains(emote)) {
                added.add(emote);
            }
        }
        return added;
    }
}
