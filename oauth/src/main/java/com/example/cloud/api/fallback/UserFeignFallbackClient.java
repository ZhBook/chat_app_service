package com.example.cloud.api.fallback;

import com.example.cloud.api.UserFeignClient;
import com.example.cloud.data.BaseResult;
import com.example.cloud.data.security.UserInfo;
import com.example.cloud.enums.ResultCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UserFeignFallbackClient implements UserFeignClient {

    @Override
    public BaseResult getUserByUsername(String username) {
        log.error("feign远程调用系统用户服务异常后的降级方法");
        return BaseResult.failed(ResultCodeEnum.FAILED.getMessage());
    }

    @Override
    public BaseResult<UserInfo> getUserByMobile(String mobile) {
        log.error("feign远程调用系统用户服务异常后的降级方法");
        return BaseResult.failed(ResultCodeEnum.FAILED.getMessage());
    }
}
