package com.lingbao.nettyroom.entity;

import lombok.Data;

/**
 * @author lingbao08
 * @DESCRIPTION
 * @create 2019-02-24 17:54
 **/
@Data
public class ListGroupMembersResponsePacket extends Packet {

    private String message;

    @Override
    public Byte getCommand() {
        return Command.LIST_GROUP_RES;
    }
}
