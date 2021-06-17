package com.lingbao.junit;

import junitparams.JUnitParamsRunner;
import lombok.extern.slf4j.Slf4j;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.springframework.beans.BeansException;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;
import org.springframework.web.context.ServletContextAware;

import javax.servlet.ServletContext;

/**
 * @author lingbao08
 * @desc
 * @date 5/18/21 22:56
 **/

@Slf4j
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
        classes = JunitApplication.class
)
@RunWith(JUnitParamsRunner.class)
public class BaseTest implements ApplicationContextAware, EnvironmentAware {


    protected ApplicationContext applicationContext;
    protected Environment environment;

    public BaseTest() {
    }


    @ClassRule
    public static final SpringClassRule SPRING_CLASS_RULE = new SpringClassRule();

    @Rule
    public final SpringMethodRule springMethodRule = new SpringMethodRule();


    public ApplicationContext getApplicationContext() {
        return this.applicationContext;
    }

    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public Environment getEnvironment() {
        return this.environment;
    }

    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }


    public static void print(String str) {
        System.out.println("--------打印开始---------");
        System.out.println("打印内容：" + str);
        System.out.println("--------打印结束---------");
    }
}
