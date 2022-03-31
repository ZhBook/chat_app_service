package com.example.cloud.secruity.token;

import com.example.cloud.constant.SecurityConstant;
import com.example.cloud.data.security.RedisKeyGenerator;
import com.example.cloud.data.security.SecureUserToken;
import com.example.cloud.data.security.UserInfo;
import com.example.cloud.exception.TokenValidationException;
import com.example.cloud.utils.JwtUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * Security User Token 操作服务
 */
@Service
public class SecureUserTokenService {

    @Resource
    private RedisTemplate<String, SecureUserToken> redisTemplate;

    /**
     * 创建 Token
     */
    public SecureUserToken createToken(UserInfo secureUser) {
        SecureUserToken userToken = new SecureUserToken();
        //todo :增加claims
        userToken.setToken(JwtUtil.create(secureUser.getId().toString(), secureUser.getUsername(), null));
        userToken.setSecureUser(secureUser);
        return userToken;
    }

    /**
     * 存储 Token
     */
    public String saveToken(SecureUserToken userToken) {
        // 使用用户名的md5作为唯一token的key
        String username = userToken.getSecureUser().getUsername();
        String key = DigestUtils.md5DigestAsHex(username.getBytes());
        redisTemplate.opsForValue().set(RedisKeyGenerator.getLoginTokenKey(key), userToken, SecurityConstant.TOKEN_EXPIRE_TIME_SECOND, TimeUnit.SECONDS);
        return key;
    }

    /**
     * 验证 Token
     */
    public SecureUserToken verifyToken(String key, String token) throws TokenValidationException {
        SecureUserToken secureUserToken = taskToken(key);
        if (null == secureUserToken) {
            throw new TokenValidationException("token expired");
        }
        if (!Objects.equals(secureUserToken.getToken(), token)) {
            throw new TokenValidationException("jwt token mismatching");
        }
        JwtUtil.parse(secureUserToken.getToken());
        return secureUserToken;
    }

    /**
     * 获取 Token
     */
    public SecureUserToken taskToken(String key) {
        return redisTemplate.opsForValue().get(RedisKeyGenerator.getLoginTokenKey(key));
    }

    /**
     * 销毁 Token
     */
    public void destroyToken(String key) {
        redisTemplate.delete(RedisKeyGenerator.getLoginTokenKey(key));
    }

}
