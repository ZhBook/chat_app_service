package com.tensua.api.fallback;

import com.tensua.api.UserFeignClient;
import com.tensua.data.BaseResult;
import com.tensua.data.security.UserInfo;
import com.tensua.enums.ResultCodeEnum;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserFeignFallbackClient implements UserFeignClient {

    @Override
    public BaseResult<UserInfo> getUserByUsername(String username) {
        log.error("feign远程调用系统用户服务异常后的降级方法");
        return BaseResult.failed(ResultCodeEnum.FAILED.getMessage());
    }

    @Override
    public BaseResult<UserInfo> getUserByMobile(String mobile) {
        log.error("feign远程调用系统用户服务异常后的降级方法");
        return BaseResult.failed(ResultCodeEnum.FAILED.getMessage());
    }
}
