package com.lingbao.base.utils;

/**
 * @author lingbao08
 * @DESCRIPTION
 * @create 3/14/21 23:53
 **/

import com.alibaba.fastjson.JSON;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import groovy.lang.Script;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.BooleanUtils;

import java.util.Map;

@Slf4j
public class GroovyShellUtil {

    /**
     * 计算表达式，得出Boolean值
     * @param exp
     * @param param
     * @return
     */
    public static boolean check(String exp, Map<String, Object> param) {
        try {
            return BooleanUtils.toBoolean(String.valueOf(calc(exp, param)));
        } catch (Exception e) {
            log.error("check 表达式：{},值：{}", exp, JSON.toJSONString(param));
        }
        return false;
    }


    /**
     * 计算表达式
     * @param exp
     * @param param
     * @return
     */
    public static Object calc(String exp, Map<String, Object> param) {
        if (param.values().contains(null) || MapUtils.isEmpty(param)) {
            return null;
        }
        GroovyShell groovyShell = new GroovyShell();
        Script script = groovyShell.parse(exp);
        script.setBinding(new Binding(param));
        return script.run();
    }

    /**
     * 检查一个表达式是否合法
     * @param exp
     * @return
     */
    public static boolean validateExp(String exp) {
        GroovyShell groovyShell = new GroovyShell();
        try {
            groovyShell.parse(exp);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
