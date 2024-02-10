package de.jonas.emote.tracker.backend.emote;

import de.jonas.emote.tracker.backend.activity.ActivityService;
import de.jonas.emote.tracker.backend.database.Emote;
import de.jonas.emote.tracker.backend.database.OriginalEmote;
import de.jonas.emote.tracker.backend.database.Source;
import de.jonas.emote.tracker.backend.database.Streamer;
import de.jonas.emote.tracker.backend.database.UserEmote;
import de.jonas.emote.tracker.backend.model.origin.EmoteSet;
import de.jonas.emote.tracker.backend.model.origin.UserOverview7TV;
import de.jonas.emote.tracker.backend.network.wrapper.SevenTVApiWrapper;
import de.jonas.emote.tracker.backend.twitch.MessageHandler;
import de.jonas.emote.tracker.backend.user.UserRepository;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class EmoteService {

    private final SevenTVApiWrapper sevenTVApi;
    private final UserRepository userRepository;
    private final ActivityService activityService;
    private final MessageHandler messageHandler;


    public EmoteService(SevenTVApiWrapper sevenTVApi, UserRepository userRepository, ActivityService activityService,
                        MessageHandler messageHandler) {
        this.sevenTVApi = sevenTVApi;
        this.userRepository = userRepository;
        this.activityService = activityService;
        this.messageHandler = messageHandler;
    }

    public Set<Emote> addEmotes(String userId) {
        UserOverview7TV sevenTvOverview = sevenTVApi.getUserByTwitchId(userId);
        if (sevenTvOverview.getEmoteSet().getEmotes().isEmpty()) {
            throw new IllegalStateException("No seven TV emotes found for user");
        }
        Set<Emote> originals = collectOriginalEmotes(sevenTvOverview.getEmoteSet());
        Set<Emote> customs = collectUserEmotes(sevenTvOverview.getEmoteSet());
        originals.addAll(customs);
        activityService.createEmoteUpdateActivity(userRepository.getReferenceById(userId));
        return originals;
    }

    public void updateUserEmotes(String userId) {
        messageHandler.pause(userId);
        Streamer user = userRepository.getStreamerByTwitchUserId(userId);
        user.setUserEmotes(addEmotes(userId));
        userRepository.saveAndFlush(user);
        messageHandler.start(userId);
    }

    private Set<Emote> collectOriginalEmotes(EmoteSet emotes) {
        return emotes.getEmotes()
            .stream()
            .filter(emote -> emote.getData().getName().equals(emote.getName()))
            .map(emote -> new OriginalEmote()
                .setId(emote.getId())
                .setName(emote.getData().getName())
                .setSource(Source.SEVENTV))
            .collect(Collectors.toSet());
    }

    private Set<Emote> collectUserEmotes(EmoteSet emotes) {
        return emotes.getEmotes()
            .stream()
            .filter(emote -> !emote.getData().getName().equals(emote.getName()))
            .map(emote -> new UserEmote()
                .setId(emote.getId())
                .setName(emote.getName())
                .setSource(Source.SEVENTV))
            .collect(Collectors.toSet());
    }

}
