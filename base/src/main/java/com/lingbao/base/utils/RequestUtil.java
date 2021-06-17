package com.lingbao.base.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 这个工具类是用来获取request的，最初是在切面中获取request信息的。
 * @author lingbao08
 * @DESCRIPTION
 * @create 2020-06-26 07:35
 **/

public class RequestUtil {

    public static String header(String headerKey) {
        return ((HttpServletRequest) RequestContextHolder.getRequestAttributes()
                .resolveReference(RequestAttributes.REFERENCE_REQUEST))
                .getHeaders(headerKey).nextElement();
    }

    public static String getRealIp() {
        HttpServletRequest request = (HttpServletRequest) RequestContextHolder.getRequestAttributes()
                .resolveReference(RequestAttributes.REFERENCE_REQUEST);
        String remoteAddr = request.getHeader("X-Real-IP");
        remoteAddr = StringUtils.isNotEmpty(remoteAddr) ? remoteAddr : request.getRemoteAddr();
        return remoteAddr.split(",")[0];
    }


    /**
     * 这个不对，需要测试并修改
     * @return
     */
    public static HttpServletRequest request() {
        return (HttpServletRequest) RequestContextHolder.getRequestAttributes()
                .resolveReference(RequestAttributes.REFERENCE_REQUEST);
    }

    /**
     * 这个不对，需要测试并修改
     *
     * @return
     */
    public static String body() {
        return request().getServletPath();
    }


    /**
     * 返回body指定类型对象
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T body(Class<T> clazz) {
        return JSONObject.parseObject(request().getServletPath(), clazz);
    }
}
