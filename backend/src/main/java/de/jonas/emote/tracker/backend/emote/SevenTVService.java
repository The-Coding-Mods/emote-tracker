package de.jonas.emote.tracker.backend.emote;

import de.jonas.emote.tracker.backend.database.Emote;
import java.util.Collection;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class SevenTVService {

    public static String buildRegexString(Collection<Emote> emotes) {
        String regex = "(?:^|(?<=\\\\s))(";
        regex += String.join("|", emotes.stream().map(Emote::getName).collect(Collectors.toSet()));
        regex += ")(?:$|(?=\\\\s))";
        return regex;
    }

}
