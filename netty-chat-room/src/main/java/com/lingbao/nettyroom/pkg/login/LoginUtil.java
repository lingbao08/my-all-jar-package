package com.lingbao.nettyroom.pkg.login;


import com.lingbao.nettyroom.pkg.Attributes;
import io.netty.channel.Channel;
import io.netty.util.Attribute;

/**
 * @author lingbao08
 * @DESCRIPTION
 * @create 2019-02-22 20:01
 **/

public class LoginUtil {

    public static void markAsLogin(Channel channel) {
        channel.attr(Attributes.LOGIN).set(true);
    }

    public static boolean hasLogin(Channel channel) {
        Attribute<Boolean> loginAttr = channel.attr(Attributes.LOGIN);

        return loginAttr.get() != null;
    }
}
