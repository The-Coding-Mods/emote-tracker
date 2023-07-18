package user;

import de.jonas.emote.tracker.backend.api.model.Emote;
import de.jonas.emote.tracker.backend.model.database.EmoteCountMap;
import de.jonas.emote.tracker.backend.repository.EmoteCountRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final EmoteCountRepository countRepository;

    public UserService(EmoteCountRepository countRepository) {
        this.countRepository = countRepository;
    }

    public List<Emote> getTop5Emotes(String userId) {
        List<EmoteCountMap> originalMap = countRepository.getEmoteCountMapsByUserTwitchUserIdOrderByCountDesc(userId);
        return new ArrayList<>();
    }

    public List<Emote> getBottom5Emotes(String userId) {
        List<EmoteCountMap> originalMap = countRepository.getEmoteCountMapsByUserTwitchUserIdOrderByCount(userId);

        return new ArrayList();
    }
}
