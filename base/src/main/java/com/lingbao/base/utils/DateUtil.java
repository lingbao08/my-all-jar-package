package com.lingbao.base.utils;

import org.apache.commons.lang3.ObjectUtils;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Optional;

/**
 * @author lingbao08
 * @DESCRIPTION
 * @create 2020-06-26 08:31
 **/

public class DateUtil {

    protected static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    protected static final DateTimeFormatter formatterYMD = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    protected static final DateTimeFormatter formatterHMS = DateTimeFormatter.ofPattern("HH:mm:ss");

    private static final ZoneId zone = ZoneId.systemDefault();

    public static long betweenTomorrow() {
        return betweenTomorrow(new Date());
    }

    /**
     * 获取指定时间距离第二天0点的秒数
     *
     * @param date
     * @return
     */
    public static long betweenTomorrow(Date date) {
        Instant start = date.toInstant();
        Instant end = dateEnd(date).toInstant();
        return Duration.between(start, end).getSeconds();
    }

    /**
     * 获取一天的末尾时间
     *
     * @param date
     * @return
     */
    public static Date dateEnd(Date date) {
        return new Date(date.toInstant().atZone(zone).toLocalDate().plusDays(1)
                .atStartOfDay().atZone(zone).toInstant().toEpochMilli() - 1);
    }


    /**
     * 获取年月日字符串
     *
     * @param date
     * @return
     */
    public static String toYMDString(Date date) {

        return LocalDateTime.ofInstant(date.toInstant(), zone).format(formatterYMD);

    }

    /**
     * 获取时分秒字符串
     *
     * @param date
     * @return
     */
    public static String toHMSString(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), zone).format(formatterHMS);

    }

    /**
     * 获取日期字符串
     *
     * @param date
     * @return
     */
    public static String toString(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), zone).format(formatter);
    }

    /**
     * 获取日期字符串
     *
     * @param mills
     * @return
     */
    public static String toString(long mills) {

        return LocalDateTime.ofInstant(Instant.ofEpochMilli(mills), zone).format(formatter);
    }

    /**
     * 获取当前时间的字符串
     *
     * @return
     */
    public static String toStr() {
        return LocalDateTime.now().format(formatter);
    }


    /**
     * 校验时间是否有交集
     *
     * @param start1
     * @param end1
     * @param start2
     * @param end2
     * @return
     */
    public static boolean checkUnion(Date start1, Date end1, Date start2, Date end2) {


        Date min = Date.from(LocalDate.MIN.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date max = Date.from(LocalDate.MAX.atStartOfDay(ZoneId.systemDefault()).toInstant());
        start1 = Optional.ofNullable(start1).orElse(min);
        start2 = Optional.ofNullable(start2).orElse(min);
        end1 = Optional.ofNullable(end1).orElse(max);
        end2 = Optional.ofNullable(end2).orElse(max);


        long s1 = start1.getTime(), s2 = start2.getTime(), e1 = end1.getTime(), e2 = end2.getTime();
        return ((s1 >= s2) && s1 < e2) || ((s1 > s2) && s1 <= e2) ||
                ((s2 >= s1) && s2 < e1) || ((s2 > s1) && s2 <= e1);
    }


    /**
     * date1大于date2
     *
     * @param date1
     * @param date2
     * @return
     */
    public static boolean compare(Date date1, Date date2) {
        return ObjectUtils.allNotNull(date1, date2)
                && date1.getTime() - date2.getTime() > 0;
    }

    public static Date max(Date date1, Date date2) {
        return compare(date1, date2) ? date1 : date2;
    }

    public static Date min(Date date1, Date date2) {
        return compare(date1, date2) ? date2 : date1;
    }


    public static Date toDate(String dateStr) {
        return Date.from(LocalDateTime.parse(dateStr, formatter).atZone(zone).toInstant());
    }

    public static void main(String[] args) {

        System.out.println(toYMDString(new Date()));
    }

}
