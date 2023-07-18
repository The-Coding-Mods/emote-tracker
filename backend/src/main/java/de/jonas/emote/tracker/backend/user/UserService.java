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

    public List<Emote> getTop5Emotes(String userId) {
        return countRepository.getEmoteCountMapsByUserTwitchUserIdOrderByCountDesc(userId).subList(0, 5).stream()
            .map(converter::convert).toList();

    }

    public List<Emote> getBottom5Emotes(String userId) {
        return countRepository.getEmoteCountMapsByUserTwitchUserIdOrderByCount(userId).subList(0, 5).stream()
            .map(converter::convert).toList();
    }
}
