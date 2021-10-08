package com.example.cloud.filter;

import com.example.cloud.service.AuthService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author zhouhd
 * @since 2021/10/8 10:00
 **/
@Component
public class TokenGlobalFilter implements GlobalFilter, Ordered {

    @Autowired
    AuthService authService;

    /**
     * @param exchange
     * @param chain
     * @return
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String authorizationHeader = authService.getAuthorizationHeader(request);

        //没有authorizationHeader参数直接过滤，否则就放行，留给oauth校验
        if (StringUtils.isEmpty(authorizationHeader)) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
/*
        long expire = authService.getExpire(authorizationHeader);
        if (expire < 0) {
            exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
            return exchange.getResponse().setComplete();
        }*/

        return chain.filter(exchange);
    }


    @Override
    public int getOrder() {
        return 0;
    }

}
