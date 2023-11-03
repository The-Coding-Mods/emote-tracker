package de.jonas.emote.tracker.backend.emote;

import de.jonas.emote.tracker.backend.databasev2.Source;
import de.jonas.emote.tracker.backend.databasev2.UserEmote;
import de.jonas.emote.tracker.backend.model.origin.UserOverview7TV;
import de.jonas.emote.tracker.backend.network.wrapper.SevenTVApiWrapper;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class SevenTVService {
    private final SevenTVApiWrapper sevenTVApi;

    public SevenTVService(SevenTVApiWrapper sevenTVApi) {
        this.sevenTVApi = sevenTVApi;
    }

    public static String buildRegexString(Collection<UserEmote> emotes) {
        String regex = "(?:^|(?<=\\\\s))(";
        regex += String.join("|", emotes.stream().map(UserEmote::getCustomEmoteName).collect(Collectors.toSet()));
        regex += ")(?:$|(?=\\\\s))";
        return regex;
    }

    public UserOverview7TV get7TvUserOverview(String userId) {
        return sevenTVApi.getUserByTwitchId(userId);
    }

    public Set<UserEmote> getSevenTVEmotes(String userId) {
        if (userId == null) {
            return Collections.emptySet();
        }
        UserOverview7TV overview = get7TvUserOverview(userId);
        return overview.getEmoteSet().getEmotes().stream()
            .map(emote -> new UserEmote(emote.getName(), emote.getId(), emote.getData().getName(), Source.SEVENTV))
            .collect(Collectors.toSet());
    }

}
