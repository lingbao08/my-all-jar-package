package com.lingbao.nettyroom.anno;


import com.lingbao.nettyroom.constant.CommandType;

import java.lang.annotation.*;

/**
 * 这个注解暂时作罢
 */
@Inherited
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Handler {

    CommandType command();
}
