package com.tensua.operator.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;


@Slf4j
public final class WebUtil {


    public static final String ACCESS_TOKEN_KEY = "Authorization";

    public static final String REQ_ATTR_LOGIN_USER_KEY = "userBeanRequest";

    private WebUtil(){}


    public static HttpServletRequest getCurrentServletRequest(){
        ServletRequestAttributes requestAttributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (null == requestAttributes) {
            log.info("获取ServletRequestAttributes为null");
            return null;
        }
        return requestAttributes.getRequest();
    }

    /**
     * 获取 HttpServletRequest 对象
     *
     * @return
     */
    public static HttpServletRequest getRequest() {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return servletRequestAttributes.getRequest();
    }
}
