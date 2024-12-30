package com.tensua.blogservice.data.security;


public class RedisKeyGenerator {

    public static final String SEPARATOR = ":";

    /**
     * 登陆次数key
     */
    public static final String LOGIN_TIMES_KEY = "service:login:times:";

    /**
     * 用户Token统一前缀
     */
    public static final String TOKEN_PREFIX = "service:token:";

    /**
     * 验证码key
     */
    public static final String CAPTCHA_PREFIX = "captcha:";

    /**
     * 获取登录用户次数redis key
     *
     * @param clintCode
     * @param tenantId
     * @param loginName
     * @return
     */
    public static String getLoginTimesKey(String clintCode, Long tenantId, String loginName) {
        return newBuilder(64).append(LOGIN_TIMES_KEY).append(clintCode).append(SEPARATOR).append(tenantId).append(SEPARATOR).append(loginName).toString();
    }

    /**
     * 获取登陆用户信息的缓存key
     *
     * @param loginToken
     * @return
     */
    public static String getLoginTokenKey(String loginToken) {
        return newBuilder(64).append(TOKEN_PREFIX).append(loginToken).toString();
    }

    /**
     * 获取验证码缓存key
     *
     * @param captchaKey
     * @return
     */
    public static String getCaptchaKey(String captchaKey) {
        return newBuilder(64).append(CAPTCHA_PREFIX).append(captchaKey).toString();
    }

    /**
     * 统一用这个方法来new StringBuilder，必须预估字符串长度
     *
     * @param capacity
     * @return
     */
    private static StringBuilder newBuilder(int capacity) {
        return new StringBuilder(capacity);
    }
}
