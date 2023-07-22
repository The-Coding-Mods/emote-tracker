package de.jonas.emote.tracker.backend.emote;

import de.jonas.emote.tracker.backend.model.database.Emote;
import de.jonas.emote.tracker.backend.model.database.Source;
import de.jonas.emote.tracker.backend.model.origin.UserOverview7TV;
import de.jonas.emote.tracker.backend.network.wrapper.SevenTVApiWrapper;
import de.jonas.emote.tracker.backend.user.UserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class SevenTVService {
    private final SevenTVApiWrapper sevenTVApi;
    private final UserRepository userRepository;

    public SevenTVService(SevenTVApiWrapper sevenTVApi, UserRepository userRepository) {
        this.sevenTVApi = sevenTVApi;
        this.userRepository = userRepository;
    }

    public static String buildRegexString(List<Emote> emotes) {
        String regex = "(?:^|(?<=\\\\s))(";
        regex += String.join("|", emotes.stream().map(Emote::getName).collect(Collectors.toSet()));
        regex += ")(?:$|(?=\\\\s))";
        return regex;
    }

    public UserOverview7TV get7TvUserOverview(String userId) {
        return sevenTVApi.getUserByTwitchId(userId);
    }

    public List<Emote> getSevenTVEmotes(UserOverview7TV userOverview) {
        if (userOverview == null) {
            return new ArrayList<>();
        }
        return userOverview.getEmoteSet().getEmotes().stream()
            .map(emote -> new Emote()
                .setId(emote.getId())
                .setName(emote.getName())
                .setSource(Source.SEVENTV))
            .toList();

    }

    public List<Emote> getSevenTVEmotes(String userId) {
        if (userId == null) {
            return new ArrayList<>();
        }
        UserOverview7TV overview = get7TvUserOverview(userId);
        return overview.getEmoteSet().getEmotes().stream()
            .map(emote -> new Emote()
                .setId(emote.getId())
                .setName(emote.getName())
                .setSource(Source.SEVENTV))
            .toList();

    }

}
