package com.tensua.utils;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.tensua.constant.BaseConstant;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;


@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class WebUtil {


    /**
     * 获取 HttpServletRequest 对象
     *
     * @return
     */
    public static HttpServletRequest getRequest() {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return servletRequestAttributes.getRequest();
    }


    /**
     * 获取 HttpServletResponse 对象
     *
     * @return
     */
    public static HttpServletResponse getResponse() {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return servletRequestAttributes.getResponse();
    }


    /**
     * 获取 HttpServletSession 对象
     *
     * @return
     */
    public static HttpSession getSession() {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return servletRequestAttributes.getRequest().getSession();
    }


    /**
     * 获取 Request 请求参数
     *
     * @return
     */
    public static String getParameter(String paramName) {
        return getRequest().getParameter(paramName);
    }


    /**
     * 获取 Request Attribute
     *
     * @param attrName
     * @return
     */
    public static Object getAttribute(String attrName) {
        return getRequest().getAttribute(attrName);
    }

    /**
     * 获取 Request Body 请求参数
     *
     * @return
     */
    public static JSONObject getBodyParameters() {
        try {
            InputStreamReader reader = new InputStreamReader(getRequest().getInputStream(), StandardCharsets.UTF_8);
            char[] buff = new char[1024];
            int length = 0;
            String body = null;
            while ((length = reader.read(buff)) != -1) {
                body = new String(buff, 0, length);
            }
            return JSON.parseObject(body);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * HttpServletResponse 对象写出数据
     *
     * @param msg
     * @throws IOException
     */
    public static void write(String msg) throws IOException {
        HttpServletResponse response = getResponse();
        response.setHeader("Content-type", "application/json;charset=" + BaseConstant.UTF8);
        response.setCharacterEncoding(BaseConstant.UTF8);
        response.getWriter().write(msg);
    }

    /**
     * HttpServletResponse 对象写出数据
     *
     * @param msg
     * @throws IOException
     */
    public static void write(String msg, HttpServletResponse response) throws IOException {
        response.setHeader("Content-type", "application/json;charset=" + BaseConstant.UTF8);
        response.setCharacterEncoding(BaseConstant.UTF8);
        response.getWriter().write(msg);
    }


    /**
     * HttpServletResponse 对象写出 JSON 数据
     *
     * @param data
     * @throws IOException
     */
    public static void writeJson(Object data) throws IOException {
        write(JSON.toJSONString(data));
    }

    /**
     * http
     * @param data
     * @param response
     * @throws IOException
     */
    public static void writeJson(Object data, HttpServletResponse response) throws IOException {
        write(JSON.toJSONString(data), response);
    }


    /**
     * Request 请求参数
     *
     * @return
     */
    public static String getQueryParam() {
        return getRequest().getQueryString();
    }


    /**
     * Request 请求方法
     *
     * @return
     */
    public static String getMethod() {
        return getRequest().getMethod();
    }

    /**
     * Request 请求头
     *
     * @return
     */
    public static String getHeader(String name) {
        return getRequest().getHeader(name);
    }

    /**
     * Request Agent
     *
     * @return
     */
    public static String getAgent() {
        return getHeader("User-Agent");
    }

    /**
     * Request 浏览器类型
     *
     * @return
     */
    public static String getBrowser() {
        String userAgent = getAgent();
        if (userAgent.contains("Firefox")) return "火狐浏览器";
        else if (userAgent.contains("Chrome")) return "谷歌浏览器";
        else if (userAgent.contains("Trident")) return "IE 浏览器";
        else return "你用啥浏览器啊？";
    }

    /**
     * Request 访问来源 ( 客户端类型 )
     *
     * @return
     */
    public static String getOs() {
        String userAgent = getAgent();
        if (getAgent().toLowerCase().contains("windows")) return "Windows";
        else if (userAgent.toLowerCase().contains("mac")) return "Mac";
        else if (userAgent.toLowerCase().contains("x11")) return "Unix";
        else if (userAgent.toLowerCase().contains("android")) return "Android";
        else if (userAgent.toLowerCase().contains("iphone")) return "IPhone";
        else return "UnKnown, More-Info: " + userAgent;
    }

}
