package com.example.cloud.api;

import com.example.cloud.api.fallback.UserFeignFallbackClient;
import com.example.cloud.data.BaseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author 70968
 */
@FeignClient(value = "example-cloud-business", fallback = UserFeignFallbackClient.class)
public interface UserFeignClient {

    /**
     * 添加好友
     * @param friendId
     * @param message
     * @return
     */
    @PostMapping("/friends/add")
    BaseResult<Boolean> addFriend(@RequestParam("friendId") Long friendId,
                                   @RequestParam("message") String message);

}
