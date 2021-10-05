package com.example.cloud.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.fastjson.JSON;
import com.example.cloud.config.UserBlockHandler;
import com.example.cloud.pojo.Users;
import com.example.cloud.service.UserService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Reference
    private UserService userService;

    /**
     * 获取用户信息
     */
    @PostMapping("/userInfo")
    public String userInfo() {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        return JSON.toJSONString(userService.userInfo(userName));
    }

    /**
     * 测试流控规则
     */
    @PostMapping("/testFlow")
    @SentinelResource(value = "user-testFlow",
            blockHandlerClass = UserBlockHandler.class, //对应异常类
            blockHandler = "handleException",  //只负责sentinel控制台配置违规
            fallback = "handleError",   //只负责业务异常
            fallbackClass = UserBlockHandler.class)
    public String testFlow() {
        Users user = userService.userInfo("admin");
        return JSON.toJSONString(user);
    }

    /**
     * 测试降级规则
     */
    @PostMapping("/testDegrade")
    @SentinelResource(value = "user-testDegrade",
            blockHandlerClass = UserBlockHandler.class, //对应异常类
            blockHandler = "handleException",  //只负责sentinel控制台配置违规
            fallback = "handleError",   //只负责业务异常
            fallbackClass = UserBlockHandler.class)
    public String testDegrade() {
        Users user = userService.userInfo("admin");
        return JSON.toJSONString(user);
    }

}
