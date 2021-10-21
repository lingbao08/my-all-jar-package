package com.lingbao.nettyroom.packet.request;


import com.lingbao.nettyroom.anno.PacketCmd;
import com.lingbao.nettyroom.constant.CommandType;
import com.lingbao.nettyroom.packet.Packet;
import lombok.Data;


@Data
@PacketCmd(command = CommandType.LOGIN_REQUEST)
public class LoginRequestPacket extends Packet {
    private Integer userId;

    private String username;

    private String password;

//    @Override
//    public Byte getCommand() {
//
//        return LOGIN_REQUEST;
//    }
}
