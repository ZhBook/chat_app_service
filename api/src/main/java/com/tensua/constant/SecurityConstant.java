package com.tensua.constant;


public interface SecurityConstant {

    /**
     * 根据项目配置
     */
    String SERVLET_CONTEXT_PATH = "/oauth";

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
    String[] HTTP_ACT_MATCHERS = {
            "/webjars/springfox-swagger-ui/fonts/**",
            "/swagger-resources",
            "/swagger-resources/configuration/**",
            "/webjars**",
            "/swagger-ui.html",
            "/webjars/springfox-swagger-ui/**",
            SERVLET_CONTEXT_PATH + LOGIN_URL,
            SERVLET_CONTEXT_PATH + SMS_LOGIN_URL,
            "/api/captcha/generate",
            LOGIN_URL
    };

    /**
     * 不需要认证的静态资源
     */
    String[] WEB_ACT_MATCHERS = {
            "/favicon.ico"
    };


    /**
     * 是否开启验证码
     */
    Boolean IS_CAPTCHA_VERIFICATION = false;


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

    String USER_INFO = "UserInfo";

    String TOKEN_HEADER_KEY = "AccessTokenKey";

    String TOKEN_PREFIX = "Bearer ";

    String AUTHORIZATION = "Authorization";

}
