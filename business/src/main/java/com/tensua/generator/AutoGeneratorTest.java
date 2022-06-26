package com.tensua.generator;

import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.rules.DateType;

import java.util.Collections;

/**
 * @author: zhooke
 * @create: 2022-06-26 07:58
 * @description: 自动生成service
 **/
public class AutoGeneratorTest {

    private static final String PROJECT_PATH = System.getProperty("user.dir") + "/business/src/main/";

    /**
     * 数据源配置
     */
    private static final DataSourceConfig DATA_SOURCE_CONFIG = new DataSourceConfig.Builder("jdbc:p6spy:mysql://101.43.11.226:3306/blog-dev", "blogdev", "123456789,.")
            .schema("blog-dev")
            .build();

    /**
     * 全局配置
     */
    private static final GlobalConfig GLOBAL_CONFIG = new GlobalConfig.Builder()
            .disableOpenDir()
            .outputDir(PROJECT_PATH + "java")
            .author("zhooke")
            .dateType(DateType.TIME_PACK)
            .commentDate("yyyy-MM-dd")
            .build();

    /**
     * 包相关配置
     */
    private static final PackageConfig PACKAGE_CONFIG = new PackageConfig.Builder()
            .parent("com.tensua.operator.blog")
            .entity("entity")
            .service("service")
            .serviceImpl("service.impl")
            .mapper("mapper")
            .xml("mapper.xml")
            .pathInfo(Collections.singletonMap(OutputFile.xml, PROJECT_PATH + "resources/mapper/"))
            .build();

    /**
     * 模板配置
     */
    private static final TemplateConfig TEMPLATE_CONFIG = new TemplateConfig.Builder()
            .disable(TemplateType.CONTROLLER)
            .build();

    /**
     * 增强配置
     * enableActiveRecord-开启后可以直接通过实体进行insert、update等操作
     */
    private static final StrategyConfig STRATEGY_CONFIG = new StrategyConfig.Builder()
            .addInclude("config_info")

            .entityBuilder()
            .enableActiveRecord()
            .enableLombok()
            .enableChainModel()

            .mapperBuilder()
            .enableBaseResultMap()
            .build();

    public static void main(String[] args) {
        AutoGenerator generator = new AutoGenerator(DATA_SOURCE_CONFIG);
        generator.packageInfo(PACKAGE_CONFIG);
        generator.global(GLOBAL_CONFIG);
        generator.template(TEMPLATE_CONFIG);
        generator.strategy(STRATEGY_CONFIG);
        generator.execute();
    }
}
