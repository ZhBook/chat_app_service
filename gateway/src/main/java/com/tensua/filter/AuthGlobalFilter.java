package com.tensua.filter;

import com.tensua.config.IgnoreUrlsConfig;
import com.tensua.constant.SecurityConstant;
import com.tensua.data.security.RedisKeyGenerator;
import com.tensua.exception.BusinessException;
import com.tensua.utils.PatternUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 将登录用户的JWT转化成用户信息的全局过滤器
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class AuthGlobalFilter implements GlobalFilter, Ordered {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private IgnoreUrlsConfig ignoreUrlsConfig;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String tokenKey = request.getHeaders().getFirst(SecurityConstant.TOKEN_HEADER_KEY);
        String path = request.getPath().value();
        // url拦截过滤
        if (PatternUtil.matches(ignoreUrlsConfig.getUrls(), path)) {
            return chain.filter(exchange);
        }
        // 简单的token认证
        Object tokenObj = taskToken(tokenKey);
        if (Objects.isNull(tokenObj)) {
            throw new BusinessException("当前用户未登录");
        }
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }

    @Bean
    @ConditionalOnMissingBean
    public HttpMessageConverters messageConverters(ObjectProvider<HttpMessageConverter<?>> converters) {
        return new HttpMessageConverters(converters.orderedStream().collect(Collectors.toList()));
    }

    /**
     * 获取 Token
     */
    public Object taskToken(String key) {
        return redisTemplate.opsForValue().get(RedisKeyGenerator.getLoginTokenKey(key));
    }
}
