package com.lingbao.base.utils;

import com.google.common.collect.Lists;
import com.lingbao.base.constants.CityEnum;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author lingbao08
 * @DESCRIPTION
 * @create 2020-09-27 10:51
 **/

public class IdCardUtil {

    private static final int[] calcC = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
    private static final char[] calcR = {'1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'};


    /**
     * 男奇女偶
     *
     * @param birth
     * @param male  true男false女
     * @return
     */
    public static String getIdNo(String birth, boolean male) {
        return getAllIdNo(birth, male, 1).get(0);
    }

    public static String getIdNo(String birth, String areaCode, boolean male) {
        return getAllIdNo(birth, areaCode, male, 1).get(0);
    }

    /**
     * @param birth 年月日 eg:19890428
     * @param male  是否为男性
     * @param num   数量
     * @return
     */
    public static List<String> getAllIdNo(String birth, boolean male, int num) {
        return getAllIdNo(birth, CityEnum.getRandomCode(), male, num);
    }

    public static List<String> getAllIdNo(String birth, String areaCode, boolean male, int num) {
        if (num < 1) {
            return Lists.newArrayList();
        }
        int xx = male ? (num == 1 ? 1 : (num - 1) << 1) : num << 1;
        String prefix = areaCode + birth;
        List<String> list = new ArrayList<>(num);
        for (int i = 1; i <= xx; i++) {
            if ((male && i % 2 == 0) || (!male && i % 2 == 1)) {
                i++;
            }
            String prefix2 = prefix + String.format("%03d", i);
            list.add(prefix2 + calcTrailingNumber(prefix2));
        }

        return list;
    }


    /*
     * <p>18位身份证验证</p>
     * 根据〖中华人民共和国国家标准 GB 11643-1999〗中有关公民身份号码的规定，
     * 公民身份号码是特征组合码，由十七位数字本体码和一位数字校验码组成。
     * 排列顺序从左至右依次为：六位数字地址码，八位数字出生日期码，三位数字顺序码和一位数字校验码。
     * 第十八位数字(校验码)的计算方法为：
     * 1.将前面的身份证号码17位数分别乘以不同的系数。从第一位到第十七位的系数分别为：7 9 10 5 8 4 2 1 6 3 7 9 10 5 8 4 2
     * 2.将这17位数字和系数相乘的结果相加。
     * 3.用加出来和除以11，看余数是多少？
     * 4.余数只可能有0 1 2 3 4 5 6 7 8 9 10这11个数字。其分别对应的最后一位身份证的号码为1 0 X 9 8 7 6 5 4 3 2。
     * 5.通过上面得知如果余数是2，就会在身份证的第18位数字上出现罗马数字的Ⅹ。如果余数是10，身份证的最后一位号码就是2。
     */
    private static char calcTrailingNumber(String sb) {
        int[] n = new int[17];
        int result = 0;
        for (int i = 0; i < n.length; i++) {
            n[i] = Integer.parseInt(String.valueOf(sb.charAt(i)));
        }

        for (int i = 0; i < n.length; i++) {
            result += calcC[i] * n[i];
        }
        return calcR[result % 11];
    }

    public static void main(String[] args) {
        //130109198904208532
        List<String> allIdNo = getAllIdNo("19890428", true, 7);
        allIdNo.forEach(System.out::println);
    }
}
