package com.lingbao.nettyroom.pkg;

import com.lingbao.nettyroom.pkg.login.MySession;
import io.netty.util.AttributeKey;

/**
 * @author lingbao08
 * @DESCRIPTION
 * @create 2019-02-22 19:47
 **/

public interface Attributes {

    AttributeKey<Boolean> LOGIN = AttributeKey.newInstance("login");

    AttributeKey<MySession> SESSION = AttributeKey.newInstance("session");
}
