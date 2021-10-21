package com.lingbao.nettyroom.constant;

import com.lingbao.nettyroom.pkg.login.MySession;
import io.netty.util.AttributeKey;

/**
 * @author lingbao08
 * @DESCRIPTION
 * @create 2019-02-22 19:47
 **/

public interface Attributes {

    /**
     * 当前用户是否登录
     */
    AttributeKey<Boolean> LOGIN = AttributeKey.newInstance("login");

    /**
     * 当前session
     */
    AttributeKey<MySession> SESSION = AttributeKey.newInstance("session");

    /**
     * 当前用户ID
     */
    AttributeKey<Integer> userId = AttributeKey.valueOf("userId");

    /**
     * 当前群组ID
     */
    AttributeKey<String> currentGroupId = AttributeKey.valueOf("groupId");
}
