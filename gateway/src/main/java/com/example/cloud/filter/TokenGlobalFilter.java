//package com.example.cloud.filter;
//
//import com.example.cloud.exception.BasicException;
//import com.example.cloud.model.CommonCode;
//import com.example.cloud.service.AuthService;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.cloud.gateway.filter.GatewayFilterChain;
//import org.springframework.cloud.gateway.filter.GlobalFilter;
//import org.springframework.core.Ordered;
//import org.springframework.http.server.reactive.ServerHttpRequest;
//import org.springframework.stereotype.Component;
//import org.springframework.web.server.ServerWebExchange;
//import reactor.core.publisher.Mono;
//
//@Component
//public class TokenGlobalFilter implements GlobalFilter, Ordered {
//
//    @Autowired
//    AuthService authService;
//
//    @Override
//    public int getOrder() {
//        return 1;
//    }
//
//    @Override
//    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//        ServerHttpRequest request = exchange.getRequest();
//        //从header中取jwt
//        String jwtFromHeader = authService.getAuthorizationHeader(request);
//        if (StringUtils.isEmpty(jwtFromHeader)) {
//            //拒绝访问
//            throw new  BasicException(CommonCode.UNAUTHENTICATED.code(),"此操作需要登陆系统");
//
//        }
//        return chain.filter(exchange);
//    }
//
//}
