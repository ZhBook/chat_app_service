package com.tensua.api;

import com.tensua.api.fallback.UserFeignFallbackClient;
import com.tensua.data.BaseResult;
import com.tensua.data.security.UserInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "cloud-business", fallback = UserFeignFallbackClient.class)
public interface UserFeignClient {

    @GetMapping("/users/username")
    BaseResult<UserInfo> getUserByUsername(@RequestParam("username") String username);

    @GetMapping("/users/mobile/{mobile}")
    BaseResult<UserInfo> getUserByMobile(@PathVariable("mobile")String mobile);

}
