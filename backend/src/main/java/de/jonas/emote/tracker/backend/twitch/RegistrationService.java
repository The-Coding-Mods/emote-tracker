package de.jonas.emote.tracker.backend.twitch;

import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import de.jonas.emote.tracker.backend.model.origin.User;
import de.jonas.emote.tracker.backend.network.wrapper.SevenTVApiWrapper;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Service;

@Service
public class RegistrationService {
    private final SevenTVApiWrapper sevenTVApi;
    private final TwitchClient client;

    public RegistrationService(SevenTVApiWrapper sevenTVApi, Client client) {
        this.sevenTVApi = sevenTVApi;
        this.client = client.getTwitchClient();
        this.client.getEventManager().onEvent(ChannelMessageEvent.class, new MessageHandler());

    }

    public boolean register(String username) {
        if (this.client.getChat().isChannelJoined(username)) {
            return false;
        }
        this.client.getChat().joinChannel(username);
        String twitchUserId = this.client.getChat().getChannelNameToChannelId().get(username);
        getSevenTVEmotes(twitchUserId);
        return true;
    }

    public boolean unregister(String username) {
        return this.client.getChat().leaveChannel(username);
    }

    @PreDestroy
    public void destroy() {
        for (String channel : this.client.getChat().getChannels()) {
            this.client.getChat().leaveChannel(channel);
        }
        this.client.getChat().disconnect();
        this.client.close();
    }

    private void getSevenTVEmotes(String userId) {
        assert userId != null;

        User user = sevenTVApi.getUserByTwitchId(userId);
        for (var emote : user.getEmoteSet().getEmotes()) {

        }
    }

}
