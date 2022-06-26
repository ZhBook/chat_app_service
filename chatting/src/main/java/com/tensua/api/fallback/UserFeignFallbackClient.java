package com.tensua.api.fallback;

import com.tensua.api.UserFeignClient;
import com.tensua.data.BaseResult;
import com.tensua.enums.ResultCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UserFeignFallbackClient implements UserFeignClient {

    @Override
    public BaseResult<Boolean> addFriend(Long friendId, String message) {
        log.error("feign远程调用系统用户服务异常后的降级方法");
        return BaseResult.failed(ResultCodeEnum.FAILED.getMessage());
    }
}
