package com.lingbao.nettyroom.entity;

import lombok.Data;

/**
 * @author lingbao08
 * @DESCRIPTION
 * @create 2019-02-24 18:03
 **/
@Data
public class AddGroupRequestPacket extends Packet {

    private String groupId;

    @Override
    public Byte getCommand() {
        return Command.ADD_TO_GROUP_REQ;
    }
}
