package com.tensua.handler;

import com.tensua.constant.AuthConstants;
import com.tensua.data.security.RedisKeyGenerator;
import com.tensua.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * @author zhooke
 * @since 2022/2/11 14:42
 **/
@Slf4j
@Component
public class AuthLoginHandler implements WebFilter {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
//        PathPattern pattern=new PathPatternParser().parse("/**");
        //所有请求都需要通过验证
        ServerHttpRequest request = exchange.getRequest();
        String tokenKey = request.getHeaders().getFirst(AuthConstants.TOKEN_HEADER_KEY);
        log.info("发送消息的token：{}", tokenKey);

        String result = redisTemplate.opsForValue().get(RedisKeyGenerator.getLoginTokenKey(tokenKey));
        if (StringUtils.isBlank(result)) {
            throw new BusinessException("未登录");
        }
        return chain.filter(exchange);
    }
}
