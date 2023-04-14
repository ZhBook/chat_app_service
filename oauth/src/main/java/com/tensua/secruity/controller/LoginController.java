package com.tensua.secruity.controller;

import com.tensua.data.BaseResult;
import com.tensua.data.security.UserInfo;
import com.tensua.secruity.service.SecureUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author zhooke
 * @since 2023/4/12 09:03
 **/
@RestController
@RequestMapping("/oauth")
@Slf4j
public class LoginController {
    @Autowired
    private SecureUserService secureUserService;

    @PostMapping("/login")
    public BaseResult<Map<String, Object>> login(@RequestParam("username") String username,
                                                 @RequestParam("password") String password) {
        UserInfo secureUser = secureUserService.loadUserByUsername(username);
        //对加密密码进行验证
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

        if (bCryptPasswordEncoder.matches(password, secureUser.getPassword())) {
            log.info("登陆成功");
            return BaseResult.succeed();
        } else {
            throw new BadCredentialsException("密码错误");
        }
    }
}
