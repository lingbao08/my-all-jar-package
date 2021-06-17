package com.lingbao.nettyroom.entity;


import lombok.Data;

/**
 * @author lingbao08
 * @DESCRIPTION
 * @create 2019-02-22 19:43
 **/
@Data
public class MessageRequestPacket extends Packet {
    //要发送给哪个用户
    private String toUserId;

    private String msg;

    @Override
    public Byte getCommand() {
        return Command.MESSAGE_REQUEST;
    }
}
