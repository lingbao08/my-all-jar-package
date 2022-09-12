package com.lingbao;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * springboot项目的util
 * @author lingbao08
 * @date 2022-09-12 18:07:14
 */
@Component
public class SpringUtil implements ApplicationContextAware {

    private static ApplicationContext applicationContext; // Spring应用上下文环境

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringUtil.applicationContext = applicationContext;
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static Object getBean(String name) throws BeansException {
        return applicationContext.getBean(name);
    }

    public static <T> T getBean(String name, Class<T> clz) throws BeansException {
        return applicationContext.getBean(name, clz);
    }

    public static <T> T getBean(Class<T> clz) throws BeansException {
        return applicationContext.getBean(clz);
    }

    public static <T> List<T> getBeans(Class<T> clz) throws BeansException {
        return new ArrayList<>(applicationContext.getBeansOfType(clz).values());
    }

    public static <T> Map<String, T> getBeansOfType(Class<T> clz) throws BeansException {
        return applicationContext.getBeansOfType(clz);
    }

    public static String getProp(String key) {
        return getBean(Environment.class).getProperty(key);
    }

    public static String getPropOrEmpty(String key) {
        return getProp(key, "");
    }

    public static String getProp(String key, String defVal) {
        return getBean(Environment.class).getProperty(key, defVal);
    }

}
