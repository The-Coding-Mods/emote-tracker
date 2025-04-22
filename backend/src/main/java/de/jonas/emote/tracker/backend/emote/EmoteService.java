package de.jonas.emote.tracker.backend.emote;

import de.jonas.emote.tracker.backend.activity.ActivityService;
import de.jonas.emote.tracker.backend.database.*;
import de.jonas.emote.tracker.backend.model.origin.EmoteSet;
import de.jonas.emote.tracker.backend.model.origin.UserOverview7TV;
import de.jonas.emote.tracker.backend.network.wrapper.SevenTVApiWrapper;
import de.jonas.emote.tracker.backend.twitch.MessageHandler;
import de.jonas.emote.tracker.backend.user.UserRepository;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmoteService {

    private final SevenTVApiWrapper sevenTVApi;
    private final UserRepository userRepository;
    private final ActivityService activityService;
    private final MessageHandler messageHandler;
    private final EmoteRepository emoteRepository;


    public EmoteService(SevenTVApiWrapper sevenTVApi, UserRepository userRepository, ActivityService activityService,
                        MessageHandler messageHandler, EmoteRepository emoteRepository) {
        this.sevenTVApi = sevenTVApi;
        this.userRepository = userRepository;
        this.activityService = activityService;
        this.messageHandler = messageHandler;
        this.emoteRepository = emoteRepository;
    }

    public Set<Emote> addEmotes(String userId) throws IllegalStateException {
        UserOverview7TV sevenTvOverview = sevenTVApi.getUserByTwitchId(userId);
        if (sevenTvOverview.getEmoteSet().getEmotes().isEmpty()) {
            throw new IllegalStateException("No seven TV emotes found for user");
        }
        Set<Emote> originals = collectOriginalEmotes(sevenTvOverview.getEmoteSet());
        Set<Emote> customs = collectUserEmotes(sevenTvOverview.getEmoteSet());
        originals.addAll(customs);
        Set<Emote> references = new HashSet<>();
        for (Emote emote : originals) {
            final EmoteId emoteId = new EmoteId(emote.getId(), emote.getName());
            if (emoteRepository.existsById(emoteId)) {
                log.debug("Emote {} ({}, {}) already exists, getting reference", emoteId, emoteId.getId(), emote.getName());
                references.add(emoteRepository.getReferenceById(emoteId));
            } else {
                log.debug("New Emote {} ({}, {})", emoteId, emoteId.getId(), emote.getName());
                references.add(emote);
            }
        }
        activityService.createEmoteUpdateActivity(userRepository.getReferenceById(userId));
        return references;
    }

    public EmoteUpdateDTO updateUserEmotes(String userId) {
        messageHandler.pause(userId);
        Streamer user = userRepository.getStreamerByTwitchUserId(userId);
        final Set<Emote> oldEmotes = user.getUserEmotes();
        final Set<Emote> newEmotes = addEmotes(userId);
        final Set<Emote> added = getAddedEmotes(oldEmotes, newEmotes);
        final Set<Emote> removed = getRemovedEmotes(oldEmotes, newEmotes);
        final Set<UpdatedEmote> renamed = getRenamedEmotes(removed, added);
        user.setUserEmotes(newEmotes);
        userRepository.saveAndFlush(user);
        messageHandler.start(userId);
        return new EmoteUpdateDTO(added, removed, renamed);
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

    private Set<Emote> getRemovedEmotes(Set<Emote> oldEmotes, Set<Emote> newEmotes) {
        Set<Emote> removed = new HashSet<>();
        for (final var emote : oldEmotes) {
            if (!newEmotes.contains(emote)) {
                removed.add(emote);
            }
        }
        return removed;
    }

    private Set<Emote> getAddedEmotes(Set<Emote> oldEmotes, Set<Emote> newEmotes) {
        Set<Emote> added = new HashSet<>();
        for (final var emote : newEmotes) {
            if (!oldEmotes.contains(emote)) {
                added.add(emote);
            }
        }
        return added;
    }

    private Set<UpdatedEmote> getRenamedEmotes(Set<Emote> removed, Set<Emote> added) {
        Set<UpdatedEmote> renamed = new HashSet<>();
        for (final var addedEmote : added) {
            for (final var removedEmote : removed) {
                if (addedEmote.getId().equals(removedEmote.getId())) {
                    renamed.add(new UpdatedEmote(addedEmote, removedEmote.getName()));
                }
            }
        }
        Set<String> renamedIds = renamed.stream().map(Emote::getId).collect(Collectors.toSet());
        added.removeIf(e -> renamedIds.contains(e.getId()));
        removed.removeIf(e -> renamedIds.contains(e.getId()));
        return renamed;
    }
}
