package com.lingbao.nettyroom.packet.resp;

import com.lingbao.nettyroom.anno.PacketCmd;
import com.lingbao.nettyroom.constant.CommandType;
import com.lingbao.nettyroom.entity.Member;
import com.lingbao.nettyroom.packet.Packet;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author lingbao08
 * @DESCRIPTION
 * @create 2019-02-24 18:03
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@PacketCmd(command = CommandType.ADD_TO_GROUP_RES)
public class AddGroupResponsePacket extends Packet {

    /**
     * 是否成功
     */
    private boolean success;

    /**
     * 群组ID
     */
    private String groupId;

    /**
     * 原因
     */
    private String reason;

    /**
     * 添加的用户
     */
    private Member addMember;

}
