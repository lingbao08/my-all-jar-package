package com.lingbao.nettyroom.entity;

import lombok.Data;

/**
 * @author lingbao08
 * @DESCRIPTION
 * @create 2019-02-24 18:03
 **/
@Data
public class AddGroupResponsePacket extends Packet {

    private boolean success;

    private String groupId;

    private String reason;

    private String addUserId;

    private String addUserName;

    @Override
    public Byte getCommand() {
        return Command.ADD_TO_GROUP_RES;
    }
}
