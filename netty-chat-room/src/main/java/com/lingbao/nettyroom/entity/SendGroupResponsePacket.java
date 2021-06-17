package com.lingbao.nettyroom.entity;


/**
 * @author lingbao08
 * @DESCRIPTION
 * @create 2019-02-24 17:20
 **/

public class SendGroupResponsePacket extends Packet {

    @Override
    public Byte getCommand() {
        return Command.SEND_GROUP_RES;
    }
}
