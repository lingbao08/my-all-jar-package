package com.lingbao.nettyroom.packet.resp;

import com.lingbao.nettyroom.anno.PacketCmd;
import com.lingbao.nettyroom.constant.CommandType;
import com.lingbao.nettyroom.packet.Packet;
import lombok.Data;

/**
 * @author lingbao08
 * @DESCRIPTION
 * @create 2019-02-24 18:12
 **/
@Data
@PacketCmd(command = CommandType.COMMON_RES)
public class CommonResponsePacket extends Packet {

    private String message;

    private String flag;

//    @Override
//    public Byte getCommand() {
//        return Command.COMMON_RES;
//    }
}
