package com.lingbao.nettyroom.entity;

import lombok.Data;

/**
 * @author lingbao08
 * @DESCRIPTION
 * @create 2019-02-22 19:45
 **/
@Data
public class MessageResponsePacket extends Packet {

    private boolean success;

    private String msg;

    private String fromUserId;

    private String fromUserName;

    @Override
    public Byte getCommand() {
        return Command.MESSAGE_RESPONSE;
    }
}
