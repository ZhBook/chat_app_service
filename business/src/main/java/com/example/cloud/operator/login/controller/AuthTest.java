package com.example.cloud.operator.login.controller;

import com.example.cloud.enums.BaseResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhouhd
 * @since 2021/10/11 16:30
 **/
@RestController
public class AuthTest {
    @GetMapping("/auth/test")
    public BaseResult authTest(){
        return BaseResult.succeed("权限校验通过");
    }
}
