package com.tensua.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;



public interface IpAddressUtil {

    String UNKNOWN = "unknown";
    String LOCAL_IP = "127.0.0.1";

    /**
     * 获取ip地址
     *
     * @param request
     * @return
     */
    static String get(HttpServletRequest request) {

        if (null == request) {
            return UNKNOWN;
        }

        String ip = request.getHeader("x-forwarded-for");
        if (StringUtils.isNotBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
            // 多次反向代理后会有多个ip值，第一个ip才是真实ip
            if (ip.contains(",")) {
                ip = ip.split(",")[0];
            }
            if (LOCAL_IP.equals(ip)) {
                ip = null;
            }
        }
        if (StringUtils.isEmpty(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (StringUtils.isEmpty(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (StringUtils.isEmpty(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtils.isEmpty(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (StringUtils.isEmpty(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (StringUtils.isEmpty(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return StringUtils.isNotBlank(ip) ? ip : UNKNOWN;
    }
}
