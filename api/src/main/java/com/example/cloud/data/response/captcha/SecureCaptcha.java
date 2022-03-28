package com.example.cloud.data.response.captcha;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Captcha 验证码实体
 * <p>
 * Create by zhengmm on 2021/12/22 14:51
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SecureCaptcha {

    /**
     * 标识
     */
    private String key;

    /**
     * 验证码
     */
    private String code;

    /**
     * 验证图片 base64
     */
    private String image;

}
