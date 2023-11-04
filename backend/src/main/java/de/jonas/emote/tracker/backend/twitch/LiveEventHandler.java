package de.jonas.emote.tracker.backend.twitch;

import com.github.twitch4j.events.ChannelGoLiveEvent;
import de.jonas.emote.tracker.backend.user.UserService;
import java.util.function.Consumer;
import org.springframework.stereotype.Service;

@Service
public class LiveEventHandler implements Consumer<ChannelGoLiveEvent> {

    private final UserService userService;

    public LiveEventHandler(UserService userService) {
        this.userService = userService;
    }


    @Override
    public void accept(ChannelGoLiveEvent channelGoLiveEvent) {
        String userId = channelGoLiveEvent.getChannel().getId();
        userService.updateEmotes(userId);
    }
}
