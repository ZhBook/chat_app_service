package com.tensua.config.component;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * @author zhooke
 * @since 2023/3/10 17:28
 **/
@Slf4j
@Component
public class PushComponent {

    @Value("${dingtalk.accesstoken}")
    private String accesstoken;

    @Value("${dingtalk.secret}")
    private String secret;

    /**
     * 推送到钉钉
     *
     * @param commentText
     * @return
     */
    public Boolean pushDingTalk(String commentText) {
        try {
            JSONObject requestBody = new JSONObject();

            JSONObject content = new JSONObject();
            content.put("content", commentText);
            requestBody.put("text", content);
            requestBody.put("msgtype", "text");
            Long timestamp = System.currentTimeMillis();
            String stringToSign = timestamp + "\n" + secret;
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secret.getBytes("UTF-8"), "HmacSHA256"));
            byte[] signData = mac.doFinal(stringToSign.getBytes("UTF-8"));
            String sign = URLEncoder.encode(new String(Base64.getEncoder().encode(signData)), "UTF-8");
            String url = java.lang.String.format("https://oapi.dingtalk.com/robot/send?access_token=%s&timestamp=%s&sign=%s", accesstoken, timestamp, sign);
            HttpRequest httpRequest = HttpUtil.createPost(url);
            httpRequest.header("access_token", accesstoken);
            String body = httpRequest.body(requestBody.toJSONString()).executeAsync().body();
            log.info("发送钉钉推送消息结果:{}", body);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            log.error("钉钉推送消息", e);
            return Boolean.FALSE;
        } catch (InvalidKeyException e) {
            log.error("钉钉推送消息", e);
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }
}
