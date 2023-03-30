package com.tensua.secruity.handler;

import com.tensua.data.BaseResult;
import com.tensua.enums.ResultCodeEnum;
import com.tensua.utils.WebUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

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
