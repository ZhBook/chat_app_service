package com.tensua.secruity.captcha;

import com.tensua.constant.SecurityConstant;
import com.tensua.context.SpringBeanContext;
import com.tensua.data.BaseResult;
import com.tensua.enums.ResultCodeEnum;
import com.tensua.exception.CaptchaValidationException;
import com.tensua.utils.WebUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.util.Strings;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Captcha Filter 主要增加 Token 的验证
 * <p>
 */
public class SecureCaptchaSupportFilter extends OncePerRequestFilter {

    private SecureCaptchaService customCaptchaService;

    public SecureCaptchaSupportFilter() {
        this.customCaptchaService = SpringBeanContext.getBean("secureCaptchaService", SecureCaptchaService.class);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String captchaKey = (String) WebUtil.getAttribute(SecurityConstant.CAPTCHA_KEY);
        String captchaCode = (String) WebUtil.getAttribute(SecurityConstant.CAPTCHA_CODE);
        // captcha param verify empty
        if (Strings.isBlank(captchaKey) || Strings.isBlank(captchaCode)) {
            WebUtil.writeJson(BaseResult.failed(ResultCodeEnum.CAPTCHA_INVALID.getMessage()));
            return;
        }
        // captcha verify
        try {
            customCaptchaService.verifyCaptcha(captchaKey, captchaCode);
        } catch (CaptchaValidationException e) {
            WebUtil.writeJson(BaseResult.failed(ResultCodeEnum.CAPTCHA_INVALID.getMessage()));
            return;
        }
        customCaptchaService.destroyCaptcha(captchaKey);
        chain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        if (!SecurityConstant.IS_CAPTCHA_VERIFICATION) {
            return true;
        }
        //校验是否账号密码登陆，需要验证码
        if (request.getMethod().equals(SecurityConstant.LOGIN_METHOD) &&
                (request.getRequestURI().equals(SecurityConstant.LOGIN_URL)
                || request.getRequestURI().equals(SecurityConstant.SMS_LOGIN_URL))) {
            return false;
        }
        return true;
    }
}
