package com.example.cloud.handler;

import com.example.cloud.constant.AuthConstants;
import com.example.cloud.exception.BusinessException;
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
        String authorization = request.getHeaders().getFirst(AuthConstants.AUTHORIZATION_KEY);
        String jwt = authorization.replace(AuthConstants.JWT_PREFIX, "");
        log.info("发送消息的token：{}", jwt);
        // 匹配验证
//        if (pattern.matches(request.getPath().pathWithinApplication())){
//            System.out.println("custom webFilter");
//        }
        String result = redisTemplate.opsForValue().get(AuthConstants.TOKEN_ACCESS_PREFIX + jwt);
        if (StringUtils.isBlank(result)) {
            throw new BusinessException("未登录");
        }
        return chain.filter(exchange);
    }
}
