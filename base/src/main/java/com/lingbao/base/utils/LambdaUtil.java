package com.lingbao.base.utils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * 因为容易忘记，所以整理一下
 *
 * @author wx
 */
public class LambdaUtil {

//=============================== FLAT_MAP ===================================================

    /**
     * 将list<Map>打平成map
     *
     * @param list
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V> Map<K, V> flatMap(List<Map<K, V>> list) {
        return list.parallelStream()
                .flatMap(x -> x.entrySet().stream())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (x1, x2) -> x1));
    }

    /**
     * 将list<List>打平成 list
     *
     * @param list
     * @param <K>
     * @return
     */
    public static <K> List<K> flatList(List<List<K>> list) {
        return list.parallelStream().flatMap(Collection::stream).collect(Collectors.toList());
    }

    //================================== 去 重 ====================================================

    /**
     * list按照指定字段去重
     *
     * @param list     要去重的list。
     * @param function 去重的function，比如User::getName.
     * @param <T>
     * @return
     */
    public static <T> List<T> distinct(List<T> list, Function function) {

        return new ArrayList<>(list.parallelStream()
                .collect(Collectors.toMap(x -> function, Function.identity(), (x1, x2) -> x1)).values());
    }


    //================================= 拼 接 字 符 串 ==============================================

    public static <T> String concat(List<T> list, String delimiter) {
        return list.stream().filter(Objects::nonNull).map(Object::toString)
                .collect(Collectors.joining(delimiter));
    }


    //================================== 分 组 ====================================================

    /**
     * 将list按照指定字段进行分组
     * 常见于用KafkaMessage消息的分组，如果需要排序分组，
     * 使用{@link LambdaUtil#groupBy(java.util.List, java.util.function.Function, java.util.function.Function)}方法.
     *
     * @param list  要执行的list。
     * @param group 要分组的函数。比如User::getAge.
     * @param <T>
     * @param <P>
     * @return
     */
    public static <T, P> Map<P, List<T>> groupBy(List<T> list, Function<T, P> group) {
        return list.parallelStream().collect(Collectors.groupingBy(group));
    }


    public static <T, Q extends Comparable<? super Q>, P> Map<P, List<T>> groupBy(
            List<T> list, Function<T, Q> sort, Function<T, P> group) {
        return list.parallelStream()
                .sorted(Comparator.comparing(sort))
                .collect(Collectors.groupingBy(group));
    }


    public static <T, Q extends Comparable<? super Q>, P> Map<P, List<T>> groupByReversed(
            List<T> list, Function<T, Q> sort, Function<T, P> group) {
        return list.parallelStream()
                .sorted(Comparator.comparing(sort).reversed())
                .collect(Collectors.groupingBy(group));
    }

    /**
     * 指定数据结构构造器，将list分组
     *
     * @param list      要执行的list
     * @param group     要分组的函数，比如User::getSex.
     * @param collector 分组后的数据要执行的收集器，比如Collectors.toList()
     * @param <T>
     * @param <P>
     * @return
     */
    public static <T, P> Map<P, List<T>> groupByWithCollector(
            List<T> list, Function<T, P> group, Collector<T, ?, List<T>> collector) {
        return list.parallelStream()
                .collect(Collectors.groupingBy(group,
                        Collectors.mapping(Function.identity(), collector)));
    }

    public static <T, Q extends Comparable<? super Q>, P> Map<P, List<T>> groupByWithCollector(
            List<T> list, Function<T, Q> sort, Function<T, P> group, Collector<T, ?, List<T>> collector) {
        return list.parallelStream()
                .sorted(Comparator.comparing(sort))
                .collect(Collectors.groupingBy(group,
                        Collectors.mapping(Function.identity(), collector)));
    }

    public static <T, Q extends Comparable<? super Q>, P> Map<P, List<T>> groupByWithCollectorReversed(
            List<T> list, Function<T, Q> sort, Function<T, P> group, Collector<T, ?, List<T>> collector) {
        return list.parallelStream()
                .sorted(Comparator.comparing(sort).reversed())
                .collect(Collectors.groupingBy(group,
                        Collectors.mapping(Function.identity(), collector)));
    }

}