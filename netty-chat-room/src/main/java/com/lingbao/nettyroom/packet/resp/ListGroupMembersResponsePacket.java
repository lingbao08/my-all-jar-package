package com.lingbao.nettyroom.packet.resp;

import com.lingbao.nettyroom.anno.PacketCmd;
import com.lingbao.nettyroom.constant.CommandType;
import com.lingbao.nettyroom.entity.Member;
import com.lingbao.nettyroom.packet.Packet;
import lombok.Data;

import java.util.List;

/**
 * @author lingbao08
 * @DESCRIPTION
 * @create 2019-02-24 17:54
 **/
@Data
@PacketCmd(command = CommandType.LIST_GROUP_RES)
public class ListGroupMembersResponsePacket extends Packet {

    /**
     * 是否成功
     */
    private Boolean success;
    /**
     * 原因或备注
     */
    private String message;

    /**
     * 用户集合
     */
    private List<Member> members;

}
