package com.lingbao.base.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.*;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lingbao08
 * @DESCRIPTION 像解析一棵树一样解析一个对象中指定的属性的值
 * @create 3/15/21 00:32
 **/

@Slf4j
public class JSONNodeUtil {

    private static ObjectMapper objectMapper = new ObjectMapper();

    private JSONNodeUtil() {
    }

    /**
     * 这种方式适合解析指定的路径中不含数组、集合的对象
     * 如果你知道属性简单，不涉及集合，请使用这种
     *
     * @param src
     * @param fieldPath
     * @return
     */
    public static Object getValueSimple(Object src, String fieldPath) {
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("js");

        try {
            engine.eval(String.format("function get(){ return JSON.parse(args[0]).%s; }", fieldPath));
            Invocable jsInvoke = (Invocable) engine;

            return jsInvoke.invokeFunction("get", JSON.toJSONString(src));
        } catch (Exception e) {
            log.error("【getValueSimple】解析错误：{}", e);
            return null;
        }
    }


    /**
     * 给一个对象，获取属性值。path举例，user.name，然后会逐级获取，直到获取到name属性的值。
     * <p>
     * 如果你不知道结构，或者内部含有集合，使用这种
     *
     * <p>
     * 如数据结构为：
     *
     * @param src
     * @param path
     * @return List对象
     * <p>
     * public class Stu {
     * private String name;
     * private List<Book> books;
     * }
     * static class Book {
     * private String name;
     * private BigDecimal price;
     * }
     * <p>
     * 使用方法： JSONNodeUtil.getValues(new Stu(), "books.name");
     * </p>
     */
    public static List<Object> getValues(Object src, String path) {
        if (src == null) {
            log.info("【JSONNodeUtil】【getValues】要获取值的对象对为空");
            return null;
        }
        String[] split = StringUtils.split(path, ".");

        if (ArrayUtils.isEmpty(split)) {
            log.info("【JSONNodeUtil】【getValues】要获取的属性路径为空");
            return null;
        }

        List<Object> result = Lists.newArrayList();
        try {
            JsonNode jsonNode = objectMapper.readTree(
                    JSON.toJSONString(src, SerializerFeature.DisableCircularReferenceDetect));
            eval(jsonNode, path, result);
        } catch (IOException e) {
            log.error("【JSONNodeUtils】解析JSON错误：{}", e);
            return result;
        }
        Class deepType;
        if (CollectionUtils.isEmpty(result) || (deepType = PropertyUtil.getDeepType(src, path)) == null) {
            return result;
        }
        if (deepType == Date.class) {
            return result.stream().map(x -> {
                if (x != null && !x.toString().equals("0")) {
                    return new Date((Long) x);
                } else {
                    return null;
                }
            }).collect(Collectors.toList());
        }
        return result;
    }


    private static void eval(JsonNode jsonNode, String fieldPath, List<Object> result) {
        if (jsonNode == null) {
            return;
        }
        if (StringUtils.isEmpty(fieldPath)) {
            return;
        }

        if (jsonNode.getNodeType() == JsonNodeType.ARRAY) {
            ArrayNode arrayNode = (ArrayNode) jsonNode;
            for (JsonNode node : arrayNode) {
                eval(node, fieldPath, result);
            }
        } else {
            jsonNode = jsonNode.get(StringUtils.substringBefore(fieldPath, "."));
            if (jsonNode == null) {
                result.add(null);
                return;
            }
            if (jsonNode.getNodeType() == JsonNodeType.ARRAY) {
                eval(jsonNode, StringUtils.substringAfter(fieldPath, "."), result);
            } else {
                result.add(get(jsonNode));
            }
        }
    }


    /**
     * float和short在项目中不用，不考虑
     *
     * @param node
     * @return
     */
    private static Object get(JsonNode node) {

        switch (node.getNodeType()) {
            case NUMBER:
                if (node instanceof LongNode)
                    return node.asLong();
                else if (node instanceof IntNode)
                    return node.asInt();
                else if (node instanceof DoubleNode)
                    return node.asDouble();
                else if (node instanceof DecimalNode)
                    return node.decimalValue();
                break;
            case STRING:
                return node.asText();
            case BOOLEAN:
                return node.asBoolean();
        }
        return null;
    }
}
