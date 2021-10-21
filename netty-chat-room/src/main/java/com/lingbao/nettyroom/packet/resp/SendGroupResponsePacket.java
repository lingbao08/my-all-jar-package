package com.lingbao.nettyroom.packet.resp;


import com.lingbao.nettyroom.anno.PacketCmd;
import com.lingbao.nettyroom.constant.CommandType;
import com.lingbao.nettyroom.packet.Packet;

/**
 * @author lingbao08
 * @DESCRIPTION
 * @create 2019-02-24 17:20
 **/
@PacketCmd(command = CommandType.SEND_GROUP_RES)
public class SendGroupResponsePacket extends Packet {

//    @Override
//    public Byte getCommand() {
//        return Command.SEND_GROUP_RES;
//    }
}
