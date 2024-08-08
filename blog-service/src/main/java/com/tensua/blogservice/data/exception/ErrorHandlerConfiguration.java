//package com.tensua.blogservice.data.exception;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.ObjectProvider;
//import org.springframework.boot.autoconfigure.web.ServerProperties;
//import org.springframework.boot.autoconfigure.web.WebProperties;
//import org.springframework.boot.context.properties.EnableConfigurationProperties;
//import org.springframework.boot.web.reactive.error.ErrorAttributes;
//import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
//import org.springframework.context.ApplicationContext;
//import org.springframework.context.annotation.Bean;
//import org.springframework.core.Ordered;
//import org.springframework.core.annotation.Order;
//import org.springframework.http.codec.ServerCodecConfigurer;
//import org.springframework.web.reactive.result.view.ViewResolver;
//
//import java.util.Collections;
//import java.util.List;
//
///**
// * 启动加载
// * 覆盖默认的异常处理
// */
//@Slf4j
//@EnableConfigurationProperties({ServerProperties.class, WebProperties.Resources.class})
//public class ErrorHandlerConfiguration {
//
//    private final ServerProperties serverProperties;
//
//    private final ApplicationContext applicationContext;
//
//    private final WebProperties.Resources resources;
//
//    private final List<ViewResolver> viewResolvers;
//
//    private final ServerCodecConfigurer serverCodecConfigurer;
//
//    public ErrorHandlerConfiguration(ServerProperties serverProperties,
//                                     WebProperties.Resources resourceProperties,
//                                     ObjectProvider<List<ViewResolver>> viewResolversProvider,
//                                     ServerCodecConfigurer serverCodecConfigurer,
//                                     ApplicationContext applicationContext) {
//        this.serverProperties = serverProperties;
//        this.applicationContext = applicationContext;
//        this.resources = resourceProperties;
//        this.viewResolvers = viewResolversProvider.getIfAvailable(Collections::emptyList);
//        this.serverCodecConfigurer = serverCodecConfigurer;
//    }
//
//    @Bean
//    @Order(Ordered.HIGHEST_PRECEDENCE)
//    public ErrorWebExceptionHandler errorWebExceptionHandler(ErrorAttributes errorAttributes) {
//        JsonExceptionHandler exceptionHandler = new JsonExceptionHandler(
//                errorAttributes,
//                this.resources,
//                this.serverProperties.getError(),
//                this.applicationContext);
//        exceptionHandler.setViewResolvers(this.viewResolvers);
//        exceptionHandler.setMessageWriters(this.serverCodecConfigurer.getWriters());
//        exceptionHandler.setMessageReaders(this.serverCodecConfigurer.getReaders());
//        return exceptionHandler;
//    }
//
//}