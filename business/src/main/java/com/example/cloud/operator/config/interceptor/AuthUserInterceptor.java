package com.example.cloud.operator.config.interceptor;

import cn.hutool.json.JSONUtil;
import com.example.cloud.exception.BusinessException;
import com.example.cloud.operator.login.entity.UserInfo;
import com.example.cloud.operator.login.service.UserInfoService;
import com.example.cloud.operator.utils.IpAddressUtil;
import com.example.cloud.operator.utils.WebUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Objects;

/**
 * @author zhouhd
 * @since 2021/10/18 13:23
 **/
@Component
@Slf4j
public class AuthUserInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    private UserInfoService userInfoService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
         String accessToken = request.getHeader(WebUtil.ACCESS_TOKEN_KEY);
        log.info("accessToken：" + accessToken);

        //IP信息
        String ip = IpAddressUtil.get(request);

        if (StringUtils.isEmpty(accessToken)) {
            log.error("ACCESS_TOKEN为空, url=[{}], ip={}", request.getRequestURI(), ip);
            return super.preHandle(request,response,handler);
        }
        UserInfo loginUser = userInfoService.getLoginUser();
        if (Objects.isNull(loginUser)) {
            log.error("获取用户信息失败, url=[{}], ip={}, accessToken={}", request.getRequestURI(), ip, accessToken);
            return notAuth(response);
        }
        return true;
    }

    //如果验证token失败，TokenAuth，返回401错误
    private boolean notAuth(HttpServletResponse response) {
        PrintWriter out = null;
        try {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json; charset=utf-8");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            out = response.getWriter();
            BusinessException businessException = new BusinessException(401, "");
            out.write(JSONUtil.toJsonStr(businessException));
        } catch (Exception e) {
            log.error("异常", e);
        } finally {
            if (out != null) {
                out.close();
            }
        }
        return false;
    }
}
