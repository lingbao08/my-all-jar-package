package com.lingbao;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.AutowireCandidateQualifier;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Map;

/**
 * @author lingbao08
 * @date 2022-09-12 18:07:14
 */
@Slf4j
public class BeanDefinitionUtil {

    public static <T> void registerBean(SysRegisterBean registerBean) {
        // ------ 1、创建bean定义
        BeanDefinitionBuilder bdb = BeanDefinitionBuilder.genericBeanDefinition(registerBean.getRegisterClass());

        // ------ 2、初始化属性
        //（1）【构造参数】设置（如果有）
        // 简单属性
//        beanDefinitionBuilder.addConstructorArgValue("张三");
        // （2）【基本属性】设置（如果有）
        // 简单属性
        Map<String, Object> propMap = registerBean.getPropMap();
        if (propMap != null && propMap.size() > 0) {
            propMap.forEach(bdb::addPropertyValue);
        }
        String initMethod = registerBean.getInitMethod();
        if (StringUtils.isNotEmpty(initMethod)) {
            bdb.setInitMethodName(initMethod);
            bdb.setLazyInit(false);
        }
        AbstractBeanDefinition bd = bdb.getBeanDefinition();
        bd.setPrimary(registerBean.isPrimary());
//        bd.getQualifiers().add(new AutowireCandidateQualifier(Qualifier.class, registerBean.getNickname()));
        bd.addQualifier(new AutowireCandidateQualifier(Qualifier.class, registerBean.getNickname()));

        // 注入属性
//         beanDefinitionBuilder.addAutowiredProperty(nickname);

        // ------ 3、注册bean
        ConfigurableApplicationContext cac = getContext();
        BeanDefinitionRegistry bdr = (BeanDefinitionRegistry) cac.getBeanFactory();

        bdr.registerBeanDefinition(registerBean.getNickname(), bd);

    }

    private static ConfigurableApplicationContext cac;

    private static ConfigurableApplicationContext getContext() {
        if (cac == null) {
            cac = (ConfigurableApplicationContext) SpringUtil.getApplicationContext();
            if (cac == null) {
                cac = (ConfigurableApplicationContext) SpringContextUtil.getApplicationContext();
            }
        }
        return cac;
    }
}
