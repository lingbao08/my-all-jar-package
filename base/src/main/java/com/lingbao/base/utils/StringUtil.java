package com.lingbao.base.utils;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.google.common.base.CaseFormat.LOWER_CAMEL;
import static com.google.common.base.CaseFormat.LOWER_UNDERSCORE;

/**
 * 字符串工具类
 *
 * @author lingbao08
 * @DESCRIPTION
 * @create 2020-06-26 08:27
 **/

public class StringUtil {

    public static final String EMOJI_PATTERN_STR =
            "[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]";

    /**
     * 过滤所有的emoji表情
     *
     * @param word
     * @return
     */
    public static String filterEmoji(String word) {
        return word.replaceAll(EMOJI_PATTERN_STR, "");
    }


    /**
     * 按照顺序获取值，谁先有，取谁的
     *
     * @return
     */
    public static Object getPriorValue(List<Object> objects) {

        if (CollectionUtils.isEmpty(objects)) {
            return null;
        }

        return objects.stream().filter(Objects::nonNull)
                .filter(x -> StringUtils.isNotEmpty(x.toString())).findFirst().orElse(null);
    }

    /**
     * 判断字符串是否是数字开头的
     * <p>
     * 场景：用于校验默写表达式是否合法
     *
     * @param str
     * @return
     */
    public static boolean startWithNumeric(String str) {
        return !StringUtils.isEmpty(str) && str.charAt(0) >= '0' && str.charAt(0) <= '9';
    }


    /**
     * 驼峰和下划线转换
     *
     * @param isCamel
     * @param strArrs
     * @return
     */
    public static List<String> camelWord(boolean isCamel, String... strArrs) {
        if (isCamel) {
            return Arrays.stream(strArrs).map(x -> LOWER_UNDERSCORE.to(LOWER_CAMEL, x))
                    .collect(Collectors.toList());
        } else {
            return Arrays.stream(strArrs).map(x -> LOWER_CAMEL.to(LOWER_UNDERSCORE, x))
                    .collect(Collectors.toList());
        }
    }

    public static String camelWord(String word) {
        return LOWER_UNDERSCORE.to(LOWER_CAMEL, word);
    }

    public static String underScoreWord(String word) {
        return LOWER_CAMEL.to(LOWER_UNDERSCORE, word);
    }


    /**
     * 字符串拼接
     *
     * @param str
     * @param suffix
     * @return
     */
    public static String appendIgnoreEmpty(final String str, String... suffix) {
        String result = StringUtils.isEmpty(str) ? "" : str;
        if (ArrayUtils.isEmpty(suffix)) {
            return result;
        }
        return result + Arrays.stream(suffix).filter(StringUtils::isEmpty).collect(Collectors.joining());
    }


    public static String removes(String src, String... removes) {

        if(StringUtils.isEmpty(src) || ArrayUtils.isEmpty(removes)){
            return src;
        }

        for (String remove : removes) {
            src = StringUtils.remove(src, remove);
        }

        return src;
    }


    /**
     * 摘自 hive  @jodd.util.StringUtil
     * @param src
     * @param chars
     * @return
     */
    public static String removeChars(String src, char... chars) {
        int i = src.length();
        StringBuilder sb = new StringBuilder(i);

        label24:
        for(int j = 0; j < i; ++j) {
            char c = src.charAt(j);

            for (char aChar : chars) {
                if (c == aChar) {
                    continue label24;
                }
            }

            sb.append(c);
        }

        return sb.toString();
    }


}
