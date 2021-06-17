package com.lingbao.nettyroom.entity;

import lombok.Data;

import static com.lingbao.nettyroom.entity.Command.LOGIN_RESPONSE;


@Data
public class LoginResponsePacket extends Packet {
    private boolean success;

    private String cause;

    private String userId;
    private String userName;

    @Override
    public Byte getCommand() {
        return LOGIN_RESPONSE;
    }
}
