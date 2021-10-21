package com.lingbao.nettyroom.anno;


import com.lingbao.nettyroom.constant.CommandType;
import com.lingbao.nettyroom.packet.Packet;

import java.lang.annotation.*;

/**
 * 这个注解，描述该packet是执行哪种命令的
 * 会加在所有的packet上
 * @see Packet
 */
@Inherited
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface PacketCmd {

    CommandType command() ;

}
