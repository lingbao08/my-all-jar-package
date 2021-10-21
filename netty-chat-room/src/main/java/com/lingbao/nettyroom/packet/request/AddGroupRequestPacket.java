package com.lingbao.nettyroom.packet.request;

import com.lingbao.nettyroom.anno.PacketCmd;
import com.lingbao.nettyroom.constant.CommandType;
import com.lingbao.nettyroom.packet.Packet;
import lombok.Data;

/**
 * @author lingbao08
 * @DESCRIPTION
 * @create 2019-02-24 18:03
 **/
@Data
@PacketCmd(command = CommandType.ADD_TO_GROUP_REQ)
public class AddGroupRequestPacket extends Packet {

    private String groupId;

//    @Override
//    public Byte getCommand() {
//        return CommandType.ADD_TO_GROUP_REQ;
//    }
}
