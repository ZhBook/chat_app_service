package com.example.cloud.utils;

import com.alibaba.fastjson.JSON;
import com.example.cloud.exception.BusinessException;
import com.google.common.collect.Maps;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.Map;

public class JwtUtil {


    private static final String ISSUER = "Nursing-Admin";

    private static final String SIGN_KEY = "Nursing-Admin-Secret-Key";

    private static final Long VALIDITY_PERIOD = 24 * 60 * 60 * 1000L; //token有效期

    /**
     * 创建 Token
     * JWT结构: 算法.内容.签名
     */
    public static String create(String userId, String username, Map<String, Object> claims) {
        return Jwts.builder()
                //设置ID， {"jti":"userId"}
                .setId(userId)
                //签发用户，{"sub":"username"}
                .setSubject(username)
                //签发日期， {"iat": 1600178387218 }
                .setIssuedAt(new Date())
                //其他参数
                .setClaims(null)
                //token 发布者
                .setIssuer(ISSUER)
                //过期时间
                .setExpiration(new Date(System.currentTimeMillis() + VALIDITY_PERIOD))
                // 签名算法和盐
                .signWith(SignatureAlgorithm.HS512, SIGN_KEY)
                .compact();
    }

    /**
     * 解析 Token
     */
    public static Claims parse(String token) {
        try {
            return Jwts.parser().setSigningKey(SIGN_KEY).parseClaimsJws(token).getBody();
        } catch (Exception e) {
            throw new BusinessException("token validation failure");
        }
    }

    public static void main(String[] args) {
        String username = create("100014", "15352058989", Maps.newHashMap());
        Claims parse = parse(username);
        System.out.println(JSON.toJSONString(parse));
    }
}
