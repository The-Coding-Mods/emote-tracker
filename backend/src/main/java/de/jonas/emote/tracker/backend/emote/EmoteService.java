package de.jonas.emote.tracker.backend.emote;

import de.jonas.emote.tracker.backend.database.Emote;
import de.jonas.emote.tracker.backend.database.EmoteId;
import de.jonas.emote.tracker.backend.database.OriginalEmote;
import de.jonas.emote.tracker.backend.database.Source;
import de.jonas.emote.tracker.backend.database.Streamer;
import de.jonas.emote.tracker.backend.database.UserEmote;
import de.jonas.emote.tracker.backend.model.origin.EmoteSet;
import de.jonas.emote.tracker.backend.model.origin.UserOverview7TV;
import de.jonas.emote.tracker.backend.network.wrapper.SevenTVApiWrapper;
import de.jonas.emote.tracker.backend.twitch.MessageHandler;
import de.jonas.emote.tracker.backend.user.UserRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class EmoteService {

    private final SevenTVApiWrapper sevenTVApi;
    private final UserRepository userRepository;

    private final EmoteRepository emoteRepository;
    private final MessageHandler messageHandler;


    public EmoteService(SevenTVApiWrapper sevenTVApi, UserRepository userRepository, EmoteRepository emoteRepository,
                        MessageHandler messageHandler) {
        this.sevenTVApi = sevenTVApi;
        this.userRepository = userRepository;
        this.emoteRepository = emoteRepository;
        this.messageHandler = messageHandler;
    }

    public List<Emote> addEmotes(String userId) {
        UserOverview7TV sevenTvOverview = sevenTVApi.getUserByTwitchId(userId);
        if (sevenTvOverview.getEmoteSet().getEmotes().isEmpty()) {
            throw new IllegalStateException("No seven TV emotes found for user");
        }
        List<Emote> originals = collectOriginalEmotes(sevenTvOverview.getEmoteSet());
        List<Emote> customs = collectUserEmotes(sevenTvOverview.getEmoteSet());
        originals.addAll(customs);
        filterAlreadyExisting(originals);
        return emoteRepository.saveAllAndFlush(originals);
    }

    public void updateUserEmotes(String userId) {
        messageHandler.pause(userId);
        Streamer user = userRepository.getStreamerByTwitchUserId(userId);
        user.setUserEmotes(addEmotes(userId));
        userRepository.saveAndFlush(user);
        messageHandler.start(userId);
    }

    private void filterAlreadyExisting(List<Emote> emotes) {
        emotes.removeIf(emote -> emoteRepository.existsById(new EmoteId(emote.getId(), emote.getName())));
    }

    private List<Emote> collectOriginalEmotes(EmoteSet emotes) {
        return emotes.getEmotes()
            .stream()
            .filter(emote -> emote.getData().getName().equalsIgnoreCase(emote.getName()))
            .map(emote -> new OriginalEmote()
                .setId(emote.getId())
                .setName(emote.getData().getName())
                .setSource(Source.SEVENTV))
            .collect(Collectors.toList());
    }

    private List<Emote> collectUserEmotes(EmoteSet emotes) {
        return emotes.getEmotes()
            .stream()
            .filter(emote -> !emote.getData().getName().equalsIgnoreCase(emote.getName()))
            .map(emote -> new UserEmote()
                .setId(emote.getId())
                .setName(emote.getName())
                .setSource(Source.SEVENTV))
            .collect(Collectors.toList());
    }

}
