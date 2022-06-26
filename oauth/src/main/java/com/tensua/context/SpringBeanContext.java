package com.tensua.context;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Srping Bean Context
 */
@Component
public class SpringBeanContext implements ApplicationContextAware {

    /**
     * 应用上下文全局锁
     */
    private static final Object lock = new Object();

    /**
     * Spring Ioc 上下文
     */
    private static ApplicationContext applicationContext;

    /**
     * 设置 Spring 上下文
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (checkNull(SpringBeanContext.applicationContext)) {
            synchronized (lock) {
                if (checkNull(SpringBeanContext.applicationContext)) {
                    SpringBeanContext.applicationContext = applicationContext;
                }
            }
        }
    }


    /**
     * 获取 Bean 实体
     */
    public static Object getBean(String name) {
        return applicationContext.getBean(name);
    }

    /**
     * 获取 Bean 实例
     */
    public static <T> T getBean(String name, Class<T> clazz) {
        return applicationContext.getBean(name, clazz);
    }

    /**
     * 获取 Bean 实例
     */
    public static <T> T getBean(Class<T> clazz) {
        return applicationContext.getBean(clazz);
    }

    /**
     * Bean 是否存在
     */
    public static boolean containsBean(String name) {
        return applicationContext.containsBean(name);
    }

    /**
     * Bean 是否为单例
     */
    public static boolean isSingleton(String name) {
        return applicationContext.isSingleton(name);
    }

    /**
     * 获取 Bean 类型
     */
    public static Class<? extends Object> getType(String name) {
        return applicationContext.getType(name);
    }

    private boolean checkNull(ApplicationContext applicationContext) {
        return applicationContext == null;
    }
}
