package de.jonas.emote.tracker.backend.user;

import de.jonas.emote.tracker.backend.api.model.Emote;
import de.jonas.emote.tracker.backend.emote.EmoteCountRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final EmoteCountRepository countRepository;
    private final EmoteCountConverter converter;

    public UserService(EmoteCountRepository countRepository, EmoteCountConverter converter) {
        this.countRepository = countRepository;
        this.converter = converter;
    }

    public List<Emote> getTopEmotes(String userId, Integer count) {
        return countRepository.getEnabledEmotesDescOrder(userId).subList(0, count)
            .stream()
            .map(converter::convert)
            .toList();
    }

    public List<Emote> getBottomEmotes(String userId, Integer count) {
        return countRepository.getEnabledEmotesOrder(userId).subList(0, count)
            .stream()
            .map(converter::convert)
            .toList();
    }

    public List<Emote> getEmotesWithNrUsage(String userId, Integer count) {
        return countRepository.getEmotesWithLessOrEqualCount(userId, count)
            .stream()
            .map(converter::convert)
            .toList();
    }

    public List<Emote> getEmotesAboveAverage(String userId) {
        return countRepository.getEmotesAboveAverage(userId)
            .stream()
            .map(converter::convert)
            .toList();
    }

    public List<Emote> getEmotesBelowAverage(String userId) {
        return countRepository.getEmotesBelowAverage(userId)
            .stream()
            .map(converter::convert)
            .toList();
    }
}
