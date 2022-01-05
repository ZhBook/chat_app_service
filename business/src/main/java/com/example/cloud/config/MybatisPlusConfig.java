package com.example.cloud.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PerformanceInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * mybatis-plus配置
 *
 * @author Mark sunlightcs@gmail.com
 */
@Configuration
public class MybatisPlusConfig {

    /**
     * 分页插件
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }


    /**
     * SQL执行效率插件
     */
    @Bean
    @Profile({"dev"})// 设置 dev 环境开启
    public PerformanceInterceptor devPerformanceInterceptor() {
        return new PerformanceInterceptor();
    }

    /**
     * SQL执行效率插件
     */
    @Bean
    @Profile({"test"})// 设置 test 环境开启
    public PerformanceInterceptor testPerformanceInterceptor() {
//        return new PerformanceInterceptor().setWriteInLog(true);
        return new PerformanceInterceptor();

    }

}
