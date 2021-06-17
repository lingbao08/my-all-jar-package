package com.lingbao.base.utils;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * 属性获取工具类
 *
 * @author lingbao08
 * @DESCRIPTION
 * @create 2020-06-26 07:34
 **/
@Slf4j
public class PropertyUtil {

    public static Object getField(Object o, String fieldName) {
        Objects.requireNonNull(o);
        Objects.requireNonNull(fieldName);
        Field field = ReflectionUtils.findField(o.getClass(), fieldName);

        Assert.notNull(field, String.format("%s has not found in %s ,check your field is correct!", fieldName, o));

        ReflectionUtils.makeAccessible(field);
        return ReflectionUtils.getField(field, o);
    }

    public static String getFieldStr(Object o, String fieldName) {
        return Optional.ofNullable(getField(o, fieldName)).orElse("").toString();
    }

    /**
     * 设置属性，并根据属性的不同进行转换
     *
     * @param o
     * @param fieldName
     * @param value
     * @return
     */
    public static Object setField(Object o, String fieldName, Object value) {

        Field field = ReflectionUtils.findField(o.getClass(), fieldName);
        Assert.notNull(field, String.format("%s has not found in %s ,check your field is correct!", fieldName, o));

        ReflectionUtils.makeAccessible(field);
        Class<?> type = field.getType();
        if (value != null) {

            if (!type.equals(value.getClass())) {
                switch (type.getSimpleName()) {

                    case "Integer":
                        value = Integer.parseInt(value.toString());
                        break;

                    case "Long":
                        value = Long.parseLong(value.toString());
                        break;

                    case "Double":
                        value = Double.parseDouble(value.toString());
                        break;

                    case "Float":
                        value = Float.parseFloat(value.toString());
                        break;

                    case "Date":
                        if (value instanceof String) {
                            value = DateUtil.toDate(value.toString());
                        }
                        if (value instanceof Long) {
                            value = (Long) value == 0L ? null : new Date((Long) value);
                        }
                        break;

                }
            }
        }
        ReflectionUtils.setField(field, o, value);

        return o;
    }

    public static <T> T copyProperties(Object src, Class<T> clazz) {
        try {
            T t = clazz.newInstance();
            BeanUtils.copyProperties(src, t);
            return t;

        } catch (Exception e) {
            log.warn("copyProperties 异常，{}", e);
        }
        return null;
    }


    /**
     * 在对象中找属性的类型。多层级的属性用.相连。如school.teacher.name，在school类中找teacher字段，
     * 然后在teacher字段对应的类中找name属性。
     * <p>
     * 适用场景：在通用配置中，需要根据类型来赋予响应的默认值，或者进行相应的转换等操作。
     *
     * @param o         对象
     * @param fieldPath 属性
     * @return
     */
    public static Class getDeepType(Object o, String fieldPath) {

        try {

            String[] split = StringUtils.split(fieldPath, ".");
            Class<?> aClass = o.getClass();

            for (String s : split) {

                Field field = ReflectionUtils.findField(aClass, s);
                aClass = field.getType();

                if (aClass.equals(List.class)) {
                    // 当前集合的泛型类型
                    Type genericType = field.getGenericType();
                    if (genericType instanceof ParameterizedType) {
                        ParameterizedType pt = (ParameterizedType) genericType;
                        // 得到泛型里的class类型对象
                        aClass = (Class<?>) pt.getActualTypeArguments()[0];
                    }
                }

            }

            return aClass;
        } catch (Exception e) {
            log.error("【Tools】【getDeepType】请求信息为：{},fieldPath为：{},异常信息为：{}",
                    JSON.toJSONString(o), fieldPath, e);
        }
        return null;
    }

}
