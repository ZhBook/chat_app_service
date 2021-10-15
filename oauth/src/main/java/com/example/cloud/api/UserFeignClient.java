package com.example.cloud.api;

import com.example.cloud.api.fallback.UserFeignFallbackClient;
import com.example.cloud.constants.UserInfo;
import com.example.cloud.enums.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "example-cloud-business", fallback = UserFeignFallbackClient.class)
public interface UserFeignClient {

    @GetMapping("/api/v1/users/username/{username}")
    Result<UserInfo> getUserByUsername(@PathVariable("username") String username);
}
