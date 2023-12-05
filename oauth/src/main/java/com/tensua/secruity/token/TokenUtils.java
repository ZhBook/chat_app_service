package com.tensua.secruity.token;

import cn.hutool.core.lang.UUID;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;

/**
 * Token 工具类
 *
 */
public class TokenUtils {

    /**
     * 生成 AccessToken
     */
    public static String generator() {
        return UUID.fastUUID().toString(true);
    }

    /**
     * 获取 AccessToken
     */
    public static String getAccessToken(HttpServletRequest request) {
        String accessToken = request.getHeader("Authorization");
        if (StringUtils.isBlank(accessToken)) {
            accessToken = request.getParameter("access_token");
        }

        return accessToken;
    }
}
