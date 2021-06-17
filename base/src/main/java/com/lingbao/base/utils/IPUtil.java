package com.lingbao.base.utils;

import sun.net.util.IPAddressUtil;

/**
 * @author lingbao08
 * @DESCRIPTION
 * @create 2020-06-26 07:33
 **/

public class IPUtil {

    public static boolean validIPv4(String ipAddr) {
        return IPAddressUtil.isIPv4LiteralAddress(ipAddr);
    }
}
