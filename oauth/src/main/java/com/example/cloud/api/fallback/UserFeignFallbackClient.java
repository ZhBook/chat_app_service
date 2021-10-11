package com.example.cloud.api.fallback;

import com.example.cloud.api.UserFeignClient;
import com.example.cloud.enums.Result;
import com.example.cloud.enums.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author haoxr
 * @createTime 2021/4/24 21:30
 */
@Component
@Slf4j
public class UserFeignFallbackClient implements UserFeignClient {

    @Override
    public Result getUserByUsername(String username) {
        log.error("feign远程调用系统用户服务异常后的降级方法");
        return Result.failed(ResultCode.FAILED.getMessage());
    }
}
