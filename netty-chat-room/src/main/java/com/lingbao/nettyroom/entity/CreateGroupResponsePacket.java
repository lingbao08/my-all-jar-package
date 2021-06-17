package com.lingbao.nettyroom.entity;

import lombok.Data;

import java.util.List;

/**
 * @author lingbao08
 * @DESCRIPTION
 * @create 2019-02-24 16:39
 **/
@Data
public class CreateGroupResponsePacket extends Packet {

    private List<String> userNameList;

    private boolean success;

    private String groupId;


    @Override
    public Byte getCommand() {
        return Command.CREATE_GROUP_RES;
    }
}
