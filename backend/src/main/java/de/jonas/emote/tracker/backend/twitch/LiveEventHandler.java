package de.jonas.emote.tracker.backend.twitch;

import com.github.twitch4j.events.ChannelGoLiveEvent;
import de.jonas.emote.tracker.backend.user.EmoteUpdateService;
import java.util.function.Consumer;
import org.springframework.stereotype.Service;

@Service
public class LiveEventHandler implements Consumer<ChannelGoLiveEvent> {

    private final EmoteUpdateService updateService;

    public LiveEventHandler(EmoteUpdateService updateService) {
        this.updateService = updateService;
    }

    @Override
    public void accept(ChannelGoLiveEvent channelGoLiveEvent) {
        String userId = channelGoLiveEvent.getChannel().getId();
        updateService.updateUserEmotes(userId);
    }
}
