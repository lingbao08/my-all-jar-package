package com.lingbao.nettyroom.packet.request;


import com.lingbao.nettyroom.anno.PacketCmd;
import com.lingbao.nettyroom.constant.CommandType;
import com.lingbao.nettyroom.packet.Packet;
import lombok.Data;

/**
 * @author lingbao08
 * @DESCRIPTION
 * @create 2019-02-22 19:43
 **/
@Data
@PacketCmd(command = CommandType.MESSAGE_REQUEST)
public class MessageRequestPacket extends Packet {

    /**
     * 消息接受者用户ID
     */
    private Integer toUserId;

    /**
     * 接收到的消息
     */
    private String msg;

}
