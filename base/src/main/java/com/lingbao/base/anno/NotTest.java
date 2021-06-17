package com.lingbao.base.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 这个注解只是为了说明一个类或者方法并没被测试过或者测试的不完善。
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE,ElementType.METHOD})
public @interface NotTest {

    /**
     * 关于测试，想补充的内容
     * @return
     */
    String desc() default "";

}
