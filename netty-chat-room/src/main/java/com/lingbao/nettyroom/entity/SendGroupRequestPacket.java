package com.lingbao.nettyroom.entity;

/**
 * @author lingbao08
 * @DESCRIPTION
 * @create 2019-02-24 17:26
 **/

public class SendGroupRequestPacket extends Packet {
    @Override
    public Byte getCommand() {
        return Command.SEND_GROUP_REQ;
    }
}
