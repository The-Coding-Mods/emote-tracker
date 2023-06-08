package de.jonas.emote.tracker.backend.network;

import com.netflix.hystrix.HystrixCommand;
import de.jonas.emote.tracker.backend.model.origin.User;
import feign.Param;
import feign.RequestLine;

public interface SevenTVApi {

    @RequestLine("GET users/twitch/{userId}")
    HystrixCommand<User> getUserByTwitchId(@Param("userId") String userId);
}
