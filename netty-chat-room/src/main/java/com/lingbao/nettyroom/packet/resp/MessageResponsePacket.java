package com.lingbao.nettyroom.packet.resp;

import com.lingbao.nettyroom.anno.PacketCmd;
import com.lingbao.nettyroom.constant.CommandType;
import com.lingbao.nettyroom.entity.Member;
import com.lingbao.nettyroom.packet.Packet;
import lombok.Data;

/**
 * @author lingbao08
 * @DESCRIPTION
 * @create 2019-02-22 19:45
 **/
@Data
@PacketCmd(command = CommandType.MESSAGE_RESPONSE)
public class MessageResponsePacket extends Packet {

    /**
     * 是否成功
     */
    private boolean success;

    /**
     * 信息
     */
    private String msg;

    /**
     * 消息发送者用户
     */
    private Member fromMember;

}
