package de.jonas.emote.tracker.backend.network.wrapper;

import de.jonas.emote.tracker.backend.model.origin.User;
import de.jonas.emote.tracker.backend.network.SevenTVApi;
import feign.hystrix.HystrixFeign;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import org.springframework.stereotype.Service;

@Service
public class SevenTVApiWrapper {
    private static final String OFFICIAL_BASE_URL = "https://7tv.io/v3";

    private final SevenTVApi sevenTVApi;

    public SevenTVApiWrapper() {
        sevenTVApi = HystrixFeign.builder()
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .target(SevenTVApi.class, OFFICIAL_BASE_URL);
    }

    public User getUserByTwitchId(String userId) {
        return sevenTVApi.getUserByTwitchId(userId).execute();
    }

}
