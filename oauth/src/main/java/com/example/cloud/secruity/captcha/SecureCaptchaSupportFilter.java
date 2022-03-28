package com.example.cloud.secruity.captcha;

import com.example.cloud.constant.SecurityConstant;
import com.example.cloud.context.SpringBeanContext;
import com.example.cloud.data.BaseResult;
import com.example.cloud.enums.ResultCodeEnum;
import com.example.cloud.exception.CaptchaValidationException;
import com.example.cloud.utils.WebUtil;
import org.apache.logging.log4j.util.Strings;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
