package de.jonas.emote.tracker.backend.network;

import com.netflix.hystrix.HystrixCommand;
import de.jonas.emote.tracker.backend.model.origin.UserOverview7TV;
import feign.Param;
import feign.RequestLine;

public interface SevenTVApi {

    @RequestLine("GET users/twitch/{userId}")
    HystrixCommand<UserOverview7TV> getUserByTwitchId(@Param("userId") String userId);
}
