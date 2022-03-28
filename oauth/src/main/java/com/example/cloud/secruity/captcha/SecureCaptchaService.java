package com.example.cloud.secruity.captcha;

import com.example.cloud.constant.SecurityConstant;
import com.example.cloud.data.response.captcha.SecureCaptcha;
import com.example.cloud.exception.CaptchaValidationException;
import com.example.cloud.utils.RedisKeyGenerator;
import com.wf.captcha.SpecCaptcha;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Captcha 验证码服务
 * <p>
 */
@Service
public class SecureCaptchaService {

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 创 建 Captcha
     */
    public SecureCaptcha createCaptcha() {
        SpecCaptcha captcha = new SpecCaptcha(142, 38);
        String key = UUID.randomUUID().toString();
        String code = captcha.text().toLowerCase();
        saveCaptcha(key, code);
        return new SecureCaptcha(key, code, captcha.toBase64());
    }

    /**
     * 验 证 Captcha
     */
    public void verifyCaptcha(String key, String code) {
        String captcha = taskCaptcha(key);
        if (captcha == null || !captcha.equals(code)) {
            throw new CaptchaValidationException("captcha invalid");
        }
    }

    /**
     * 保存 Captcha
     * @param key
     * @param code
     */
    public void saveCaptcha(String key, String code) {
        redisTemplate.opsForValue().set(RedisKeyGenerator.getCaptchaKey(key), code, SecurityConstant.CAPTCHA_EXPIRE_TIME_SECOND, TimeUnit.SECONDS);
    }

    /**
     * 获 取 Captcha
     */
    public String taskCaptcha(String key) {
        return redisTemplate.opsForValue().get(RedisKeyGenerator.getCaptchaKey(key) + key);
    }

    /**
     * 销 毁 Captcha
     */
    public void destroyCaptcha(String key) {
        redisTemplate.delete(RedisKeyGenerator.getCaptchaKey(key));
    }
}
