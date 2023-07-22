package de.jonas.emote.tracker.backend.user;

import de.jonas.emote.tracker.backend.api.model.Emote;
import de.jonas.emote.tracker.backend.repository.EmoteCountRepository;
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
        return countRepository.getEmoteCountMapsByUserTwitchUserIdOrderByCountDesc(userId).subList(0, count)
            .stream()
            .map(converter::convert)
            .toList();

    }

    public List<Emote> getBottomEmotes(String userId, Integer count) {
        return countRepository.getEmoteCountMapsByUserTwitchUserIdOrderByCount(userId).subList(0, count)
            .stream()
            .map(converter::convert)
            .toList();
    }

    public List<Emote> getEmotesWithNrUsage(String userId, int count) {
        return countRepository.getEmoteCountMapsByUserTwitchUserIdAndCountIsLessThanEqual(userId, count)
            .stream()
            .map(converter::convert)
            .toList();
    }
}
