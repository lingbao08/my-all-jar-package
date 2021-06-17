package com.lingbao.nettyroom.entity;

import lombok.Data;

/**
 * @author lingbao08
 * @DESCRIPTION
 * @create 2019-02-24 18:12
 **/
@Data
public class CommonResponsePacket extends Packet {

    private String message;

    private String flag;

    @Override
    public Byte getCommand() {
        return Command.COMMON_RES;
    }
}
