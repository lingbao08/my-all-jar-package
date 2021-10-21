package com.lingbao.nettyroom.utils;

import java.util.Random;

/**
 * @author lingbao08
 * @DESCRIPTION
 * @create 2019-02-24 17:03
 **/

public class IDUtil {

    public static int getUserId() {
        return new Random().nextInt(10000);
    }

    public static String getGroupId(){
        return "G-"+new Random().nextInt(100);
    }

}
