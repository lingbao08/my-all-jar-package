package com.lingbao.nettyroom.packet.request;

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
@PacketCmd(command = CommandType.CREATE_GROUP_REQ)
public class CreateGroupRequestPacket extends Packet {

    private List<Integer> userIdList;

}
