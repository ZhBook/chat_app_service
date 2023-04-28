package com.tensua.component;

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
import java.nio.charset.StandardCharsets;
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

    private static final String DING_TALK_URL = "https://oapi.dingtalk.com/robot/send?access_token=%s&timestamp=%s&sign=%s";

    @Value("${dingtalk.accesstoken}")
    private String accesstoken;

    @Value("${dingtalk.secret}")
    private String secret;

    /**
     * 推送到钉钉
     *
     * @param title
     * @param text
     * @return
     */
    public Boolean pushDingTalk(String title, String text) {
        try {
            JSONObject requestBody = new JSONObject();
            JSONObject markdown = new JSONObject();
            markdown.put("title", title);
            markdown.put("text", text);
            requestBody.put("markdown", markdown);
            requestBody.put("msgtype", "markdown");
            Long timestamp = System.currentTimeMillis();
            String stringToSign = timestamp + "\n" + secret;
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            byte[] signData = mac.doFinal(stringToSign.getBytes(StandardCharsets.UTF_8));
            String sign = URLEncoder.encode(new String(Base64.getEncoder().encode(signData)), "UTF-8");
            String url = String.format(DING_TALK_URL, accesstoken, timestamp, sign);
            HttpRequest httpRequest = HttpUtil.createPost(url);
            httpRequest.header("access_token", accesstoken);
            String body = httpRequest.body(requestBody.toJSONString()).executeAsync().body();
            log.info("发送钉钉推送消息结果:{}", body);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException | InvalidKeyException e) {
            log.error("钉钉推送消息", e);
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }
}
