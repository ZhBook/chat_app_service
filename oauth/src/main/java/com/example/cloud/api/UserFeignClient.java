package com.example.cloud.api;

import com.example.cloud.api.fallback.UserFeignFallbackClient;
import com.example.cloud.entity.UserInfo;
import com.example.cloud.enums.BaseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "example-cloud-business", fallback = UserFeignFallbackClient.class)
public interface UserFeignClient {

    @GetMapping("/users/username")
    BaseResult<UserInfo> getUserByUsername(@RequestParam("username") String username);

    @GetMapping("/users/mobile/{mobile}")
    BaseResult<UserInfo> getUserByMobile(@PathVariable("mobile")String mobile);

}
