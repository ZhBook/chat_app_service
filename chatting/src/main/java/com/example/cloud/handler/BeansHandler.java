//package com.example.cloud.handler;
//
//import com.example.cloud.data.AuthConstants;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.reactive.function.server.RequestPredicates;
//import org.springframework.web.reactive.function.server.RouterFunction;
//import org.springframework.web.reactive.function.server.RouterFunctions;
//import org.springframework.web.reactive.function.server.ServerResponse;
//import reactor.core.publisher.Mono;
//
//
///**
// * @author zhooke
// * @since 2022/2/11 11:58
// **/
//@Slf4j
//@Configuration
//public class BeansHandler {
//  用于处理符合条件的请求
//    @Bean
//    public RouterFunction<ServerResponse> route() {
//        return RouterFunctions.route(
//                        RequestPredicates.all(),
//                        request -> Mono.empty()
//                )
//                .filter((serverRequest, handlerFunction) -> {
//                    String authorization = serverRequest.headers().firstHeader(AuthConstants.AUTHORIZATION_KEY);
//                    String jwt = authorization.replace(AuthConstants.JWT_PREFIX, "");
//                    log.info("token：{}", jwt);
//                    // 针对/hello 的请求进行过滤，然后在响应中添加一个Content-Type属性
//                    return handlerFunction.handle(serverRequest);
//                });
//    }
//}
