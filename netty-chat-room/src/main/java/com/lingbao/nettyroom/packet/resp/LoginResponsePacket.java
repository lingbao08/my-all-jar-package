package com.lingbao.nettyroom.packet.resp;

import com.lingbao.nettyroom.anno.PacketCmd;
import com.lingbao.nettyroom.constant.CommandType;
import com.lingbao.nettyroom.packet.Packet;
import lombok.Data;



@Data
@PacketCmd(command = CommandType.LOGIN_RESPONSE)
public class LoginResponsePacket extends Packet {
    /**
     * 是否成功
     */
    private boolean success;

    /**
     * 失败原因
     */
    private String cause;

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 用户名
     */
    private String userName;

}
