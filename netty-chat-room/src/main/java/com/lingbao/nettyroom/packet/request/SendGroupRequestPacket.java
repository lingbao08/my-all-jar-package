package com.lingbao.nettyroom.packet.request;

import com.lingbao.nettyroom.anno.PacketCmd;
import com.lingbao.nettyroom.constant.CommandType;
import com.lingbao.nettyroom.packet.Packet;

/**
 * @author lingbao08
 * @DESCRIPTION
 * @create 2019-02-24 17:26
 **/
@PacketCmd(command = CommandType.SEND_GROUP_REQ)
public class SendGroupRequestPacket extends Packet {
//    @Override
//    public Byte getCommand() {
//        return Command.SEND_GROUP_REQ;
//    }
}
