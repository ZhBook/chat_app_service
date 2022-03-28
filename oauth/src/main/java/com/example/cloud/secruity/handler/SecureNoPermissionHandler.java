package com.example.cloud.secruity.handler;

import com.example.cloud.data.BaseResult;
import com.example.cloud.enums.ResultCodeEnum;
import com.example.cloud.utils.WebUtil;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Security 用户无权限处理类
 */
@Component
public class SecureNoPermissionHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException {
        WebUtil.writeJson(BaseResult.fail(ResultCodeEnum.NOT_PERMISSION));
    }
}
