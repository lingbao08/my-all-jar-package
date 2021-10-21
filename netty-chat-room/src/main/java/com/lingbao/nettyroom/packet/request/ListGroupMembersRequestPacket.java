package com.lingbao.nettyroom.packet.request;

import com.lingbao.nettyroom.anno.PacketCmd;
import com.lingbao.nettyroom.constant.CommandType;
import com.lingbao.nettyroom.packet.Packet;
import lombok.Data;

/**
 * @author lingbao08
 * @DESCRIPTION
 * @create 2019-02-24 17:54
 **/
@Data
@PacketCmd(command = CommandType.LIST_GROUP_REQ)
public class ListGroupMembersRequestPacket extends Packet {

}
