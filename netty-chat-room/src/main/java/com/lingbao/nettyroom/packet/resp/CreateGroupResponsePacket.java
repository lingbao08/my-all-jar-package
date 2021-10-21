package com.lingbao.nettyroom.packet.resp;

import com.lingbao.nettyroom.anno.PacketCmd;
import com.lingbao.nettyroom.constant.CommandType;
import com.lingbao.nettyroom.packet.Packet;
import lombok.Data;

import java.util.List;

/**
 * @author lingbao08
 * @DESCRIPTION
 * @create 2019-02-24 16:39
 **/
@Data
@PacketCmd(command = CommandType.CREATE_GROUP_RES)
public class CreateGroupResponsePacket extends Packet {

    private List<String> userNameList;

    private boolean success;

    private String groupId;


//    @Override
//    public Byte getCommand() {
//        return Command.CREATE_GROUP_RES;
//    }
}
