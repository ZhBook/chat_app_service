package com.example.cloud.operator.utils;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;

@Slf4j
public class IpAddressUtil {
    /**
     * 获取ip地址
     *
     * @param request
     * @return
     */
    public static String get(HttpServletRequest request) {
        consoleIp(request);
        String ip = request.getHeader("X-Forwarded-For");
        if (ip != null){
            if (!ip.isEmpty() && !"unKnown".equalsIgnoreCase(ip)) {
                int index = ip.indexOf(",");
                if (index != -1) {
                    return ip.substring(0, index);
                } else {
                    return ip;
                }
            }
        }
        ip = request.getHeader("X-Real-IP");
        if (ip != null) {
            if (!ip.isEmpty() && !"unKnown".equalsIgnoreCase(ip)) {
                return ip;
            }
        }
        ip = request.getHeader("Proxy-Client-IP");
        if (ip != null) {
            if (!ip.isEmpty() && !"unKnown".equalsIgnoreCase(ip)) {
                return ip;
            }
        }
        ip = request.getHeader("WL-Proxy-Client-IP");
        if (ip != null) {
            if (!ip.isEmpty() && !"unKnown".equalsIgnoreCase(ip)) {
                return ip;
            }
        }
        ip =  request.getRemoteAddr();
        return ip.equals("0:0:0:0:0:0:0:1") ? "127.0.0.1" : ip;
    }

    private static void consoleIp(HttpServletRequest request){
        log.info("X-Forwarded-For---------------{}",request.getHeader("X-Forwarded-For"));
        log.info("X-Real-IP---------------{}",request.getHeader("X-Real-IP"));
        log.info("Proxy-Client-IP---------------{}",request.getHeader("Proxy-Client-IP"));
        log.info("WL-Proxy-Client-IP---------------{}",request.getHeader("WL-Proxy-Client-IP"));
    }
}
