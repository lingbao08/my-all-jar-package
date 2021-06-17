package com.lingbao.nettyroom.entity;


import lombok.Data;

import static com.lingbao.nettyroom.entity.Command.LOGIN_REQUEST;

@Data
public class LoginRequestPacket extends Packet {
    private Integer userId;

    private String username;

    private String password;

    @Override
    public Byte getCommand() {

        return LOGIN_REQUEST;
    }
}
