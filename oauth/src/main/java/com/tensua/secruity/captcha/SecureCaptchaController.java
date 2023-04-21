package com.tensua.secruity.captcha;

import com.tensua.data.BaseResult;
import com.tensua.data.response.captcha.SecureCaptcha;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/api/captcha")
public class SecureCaptchaController extends BaseResult {

    @Autowired
    private SecureCaptchaService customCaptchaService;

    /**
     * Captcha 生成
     */
    @GetMapping("/generate")
    public BaseResult<SecureCaptcha> createCaptcha() {
        return BaseResult.succeed(customCaptchaService.createCaptcha());
    }

}
