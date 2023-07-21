package de.jonas.emote.tracker.backend.twitch;

import com.github.twitch4j.events.ChannelGoLiveEvent;
import de.jonas.emote.tracker.backend.emote.SevenTVService;
import de.jonas.emote.tracker.backend.model.database.Emote;
import de.jonas.emote.tracker.backend.model.database.EmoteCountMap;
import de.jonas.emote.tracker.backend.model.database.User;
import de.jonas.emote.tracker.backend.user.UserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class LiveEventHandler implements Consumer<ChannelGoLiveEvent> {

    private final UserRepository userRepository;
    private final SevenTVService sevenTVService;

    public LiveEventHandler(UserRepository userRepository, SevenTVService sevenTVService) {
        this.userRepository = userRepository;
        this.sevenTVService = sevenTVService;
    }

    @Override
    public void accept(ChannelGoLiveEvent channelGoLiveEvent) {
        String userId = channelGoLiveEvent.getChannel().getId();
        User user = userRepository.getUserByTwitchUserId(userId);
        List<Emote> oldEmotes = user.getEmoteCounts()
            .stream()
            .filter(EmoteCountMap::isEnabled)
            .map(EmoteCountMap::getEmote).toList();

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
        emoteCountMap = emoteCountMap
            .stream()
            .filter(e -> removed.contains(e.getEmote()))
            .map(e -> e.setEnabled(false))
            .collect(Collectors.toSet());
        emoteCountMap.addAll(EmoteCountMap.fromEmoteList(added, user));

        user.setEmoteCounts(emoteCountMap);
        user.setEmoteRegex(SevenTVService.buildRegexString(newEmotes));

        userRepository.saveAndFlush(user);
    }
}
