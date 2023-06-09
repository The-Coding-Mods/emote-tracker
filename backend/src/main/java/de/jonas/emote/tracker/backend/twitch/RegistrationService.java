package de.jonas.emote.tracker.backend.twitch;

import com.github.twitch4j.chat.TwitchChat;
import com.github.twitch4j.helix.domain.User;
import de.jonas.emote.tracker.backend.emotes.SevenTVEmotes;
import de.jonas.emote.tracker.backend.model.database.Emote;
import de.jonas.emote.tracker.backend.model.database.Source;
import de.jonas.emote.tracker.backend.model.origin.UserOverview7TV;
import de.jonas.emote.tracker.backend.network.wrapper.SevenTVApiWrapper;
import de.jonas.emote.tracker.backend.network.wrapper.TwitchApiWrapper;
import jakarta.annotation.PreDestroy;
import java.util.Collections;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class RegistrationService {
    private final SevenTVApiWrapper sevenTVApi;
    private final TwitchApiWrapper twitchApi;
    private final TwitchChat chat;

    private final SevenTVEmotes sevenTVEmotes;

    public RegistrationService(
        SevenTVApiWrapper sevenTVApi,
        TwitchApiWrapper twitchApi,
        Client client,
        SevenTVEmotes sevenTVEmotes) {
        this.sevenTVApi = sevenTVApi;
        this.twitchApi = twitchApi;
        this.chat = client.getTwitchClient().getChat();
        this.sevenTVEmotes = sevenTVEmotes;
    }

    public boolean register(String username) {
        if (this.chat.isChannelJoined(username)) {
            return false;
        }
        this.chat.joinChannel(username);
        List<User> users =
            this.twitchApi.getUsers(null, Collections.singletonList(username)).getUsers();
        if (users.isEmpty() || !getSevenTVEmotes(username, users.get(0).getId())) {
            unregister(username);
            return false;
        }
        return true;
    }

    public boolean unregister(String username) {
        return this.chat.leaveChannel(username);
    }

    @PreDestroy
    public void destroy() {
        for (String channel : this.chat.getChannels()) {
            this.chat.leaveChannel(channel);
        }
        this.chat.disconnect();
    }

    private boolean getSevenTVEmotes(String username, String userId) {
        if (userId == null) {
            return false;
        }
        UserOverview7TV overview = sevenTVApi.getUserByTwitchId(userId);
        List<Emote> emotes =
            overview.getEmoteSet().getEmotes().stream()
                .map(emote -> new Emote(emote.getId(), emote.getName(), Source.SEVENTV))
                .toList();
        sevenTVEmotes.insertUser(
            new de.jonas.emote.tracker.backend.model.database.User(
                userId, username, overview.getId(), emotes));
        return true;
    }
}
