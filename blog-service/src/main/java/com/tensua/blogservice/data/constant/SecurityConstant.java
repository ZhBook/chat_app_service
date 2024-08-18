package com.tensua.blogservice.data.constant;

import org.springframework.security.access.ConfigAttribute;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public interface SecurityConstant {

    /**
     * 根据项目配置
     */
    String SERVLET_CONTEXT_PATH = "/users";

    /**
     * 默认的登录接口
     */
    String LOGIN_URL = SERVLET_CONTEXT_PATH + "/login";

    /**
     * SMS短信登陆接口
     */
    String SMS_LOGIN_URL = LOGIN_URL + ":sms";

    /**
     * 登录接口方式
     */
    String LOGIN_METHOD = "POST";

    /**
     * 默认的注销接口
     */
    String LOGOUT_URL = SERVLET_CONTEXT_PATH + "/logout";

    /**
     * 不需要认证的接口资源
     */
    String[] WHITELIST = {
            SMS_LOGIN_URL,
            LOGIN_URL,
            "/users/login",
            "/users/register",
            "/users/**",
            "/resources/**",
            "/upload",
            "/upload/**",
            "/blog/**",
            "/system/**",
            "/weather/**",
    };
    /**
     * 模拟权限数据。key：接口地址，value：所需权限
     */
    Map<String, ConfigAttribute> PERMISSION_MAP = new ConcurrentHashMap<>();

    /**
     * 是否开启验证码
     */
    Boolean IS_CAPTCHA_VERIFICATION = Boolean.FALSE;

    /**
     * 验证码key 参数名
     */
    String CAPTCHA_KEY = "captchaKey";

    /**
     * 验证码 参数名
     */
    String CAPTCHA_CODE = "captchaCode";

    /**
     * Captcha 过期时间
     */
    long CAPTCHA_EXPIRE_TIME_SECOND = 5 * 60;

    /**
     * Token 过期时间
     */
    long TOKEN_EXPIRE_TIME_SECOND = 60 * 60 * 12;

    String TOKEN_HEADER = "AccessToken";

    String USER_INFO = "OauthUserInfo";

    String TOKEN_PREFIX = "Bearer ";

    String AUTHORIZATION = "Authorization";
}
