//package com.tensua.blogservice.config.interceptor;
//
//import cn.hutool.json.JSONUtil;
//import com.tensua.blogservice.config.IgnoreUrlsConfig;
//import com.tensua.blogservice.data.exception.BusinessException;
//import com.tensua.blogservice.operator.login.entity.UserInfo;
//import com.tensua.blogservice.operator.login.service.UserInfoService;
//import com.tensua.blogservice.utils.IpAddressUtil;
//import com.tensua.blogservice.utils.WebUtil;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.stereotype.Component;
//import org.springframework.web.servlet.AsyncHandlerInterceptor;
//
//import javax.annotation.Resource;
//import java.io.PrintWriter;
//import java.util.List;
//import java.util.Objects;
//
///**
// * @author zhouhd
// * @since 2021/10/18 13:23
// **/
//@Component
//@Slf4j
//public class AuthUserInterceptor implements AsyncHandlerInterceptor {
//
//    @Resource
//    private UserInfoService userInfoService;
//
//    @Resource
//    private IgnoreUrlsConfig ignoreUrlsConfig;
//
//
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        String accessToken = request.getHeader(WebUtil.ACCESS_TOKEN_KEY);
////        log.info("accessToken：" + accessToken);
//
//        //IP信息
//        String ip = IpAddressUtil.get(request);
//        String requestURI = request.getRequestURI();
//        List<String> urls = ignoreUrlsConfig.getUrls();
//        Boolean flag = Boolean.FALSE;
//        for (String url : urls) {
//            if (StringUtils.equals(url, requestURI)) {
//                flag = Boolean.TRUE;
//            }
//        }
//        //todo 需要优化
//        if (flag) {
//            log.info("该访问连接不需要验证，url=[{}], ip={}", request.getRequestURI(), ip);
//            return true;
//        }
//        if (StringUtils.isEmpty(accessToken)) {
//            log.error("ACCESS_TOKEN为空, url=[{}], ip={}", request.getRequestURI(), ip);
//            return true;
//        }
//        UserInfo loginUser = userInfoService.getLoginUser();
//        request.getRequestURL();
//        if (Objects.isNull(loginUser)) {
//            log.error("获取用户信息失败, url=[{}], ip={}, accessToken={}", request.getRequestURI(), ip, accessToken);
//            return notAuth(response);
//        }
//        return true;
//    }
//
//    //如果验证token失败，TokenAuth，返回401错误
//    private boolean notAuth(HttpServletResponse response) {
//        PrintWriter out = null;
//        try {
//            response.setCharacterEncoding("UTF-8");
//            response.setContentType("application/json; charset=utf-8");
//            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//            out = response.getWriter();
//            BusinessException businessException = new BusinessException(401, "token验证失败");
//            out.write(JSONUtil.toJsonStr(businessException));
//        } catch (Exception e) {
//            log.error("异常", e);
//        } finally {
//            if (out != null) {
//                out.close();
//            }
//        }
//        return false;
//    }
//}
