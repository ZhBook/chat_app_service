package com.tensua.blogservice.utils;

import com.alibaba.fastjson2.JSON;
import com.google.common.collect.Lists;
import com.tensua.blogservice.data.exception.BusinessException;
import com.tensua.blogservice.data.security.OauthUserInfo;
import com.tensua.blogservice.data.security.RedisKeyGenerator;
import com.tensua.blogservice.data.security.SecureUserToken;
import com.tensua.blogservice.operator.login.entity.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.Instant;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class JwtUtil {

    @Resource
    private JwtEncoder encoder;

    @Resource
    private JwtDecoder jwtDecoder;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    //常量
    public static final long EXPIRE = 60 * 60 * 4; //token过期时间,4个小时

    //生成token字符串的方法
    public String getToken(String userName) {
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plusSeconds(EXPIRE))
                .subject(userName)
                .claim("scope", Lists.newArrayList())//todo 待优化
                .build();
        return this.encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    //验证token字符串是否是有效的  包括验证是否过期
    public boolean checkToken(String jwtToken) {
        if (jwtToken == null || jwtToken.isEmpty()) {
            log.error("Jwt is empty");
            return false;
        }
        try {
            Jwt decode = jwtDecoder.decode(jwtToken);
            return Objects.requireNonNull(decode.getExpiresAt()).isAfter(Instant.now());
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

    public Map<String, Object> getTokenBody(String jwtToken) {
        if (jwtToken == null || jwtToken.isEmpty()) {
            log.error("Jwt is empty");
            return null;
        }
        try {
            Jwt decode = jwtDecoder.decode(jwtToken);
            Map<String, Object> headers = decode.getHeaders();
            return decode.getClaims();
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    public void insertToken(String token, UserInfo userInfo) {
        String tokenPrefix = token.split("\\.")[0];
        SecureUserToken secureUserToken = new SecureUserToken();
        secureUserToken.setToken(token);
        secureUserToken.setSecureUser(new OauthUserInfo(userInfo, Lists.newArrayList()));
        redisTemplate.opsForValue().set(RedisKeyGenerator.getLoginTokenKey(tokenPrefix), JSON.toJSONString(secureUserToken), EXPIRE, TimeUnit.SECONDS);
    }

    /**
     * 获取 Token
     */
    public SecureUserToken taskToken(String key) {
        Object redisResult = redisTemplate.opsForValue().get(RedisKeyGenerator.getLoginTokenKey(key));
        if (Objects.isNull(redisResult)) {
            throw new BusinessException("登录过期，请重新登陆");
        }
        return JSON.parseObject(redisResult.toString(), SecureUserToken.class);
    }
}