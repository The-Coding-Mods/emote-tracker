package de.jonas.emote.tracker.backend.twitch;

import com.github.twitch4j.events.ChannelGoLiveEvent;
import de.jonas.emote.tracker.backend.emote.EmoteService;
import de.jonas.emote.tracker.backend.user.UserService;
import java.util.function.Consumer;
import org.springframework.stereotype.Service;

@Service
public class LiveEventHandler implements Consumer<ChannelGoLiveEvent> {

    private final EmoteService userService;

    public LiveEventHandler(EmoteService userService) {
        this.userService = userService;
    }


    @Override
    public void accept(ChannelGoLiveEvent channelGoLiveEvent) {
        String userId = channelGoLiveEvent.getChannel().getId();
        userService.updateUserEmotes(userId);
    }
}
