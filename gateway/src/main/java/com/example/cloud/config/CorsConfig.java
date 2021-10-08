//package com.example.cloud.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.Ordered;
//import org.springframework.core.annotation.Order;
//import org.springframework.web.cors.CorsConfiguration;
//import org.springframework.web.cors.reactive.CorsWebFilter;
//import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
//
//@Configuration
//public class CorsConfig {
//    private static final String ALL = "*";
//
//    @Order(Ordered.HIGHEST_PRECEDENCE)
//    @Bean
//    public CorsWebFilter corsFilter() {
//        // 基于url跨域，选择reactive包下的
//        UrlBasedCorsConfigurationSource source=new UrlBasedCorsConfigurationSource();
//        // 跨域配置信息
//        CorsConfiguration corsConfiguration = new CorsConfiguration();
//        // 允许跨域的头
//        corsConfiguration.addAllowedHeader(ALL);
//        // 允许跨域的请求方式
//        corsConfiguration.addAllowedMethod(ALL);
//        // 允许跨域的请求来源
//        corsConfiguration.addAllowedOrigin(ALL);
//        // 是否允许携带cookie跨域
////        corsConfiguration.setAllowCredentials(true);
//
//        // 任意url都要进行跨域配置
//        source.registerCorsConfiguration("/**",corsConfiguration);
//        return new CorsWebFilter(source);
//    }
//}
