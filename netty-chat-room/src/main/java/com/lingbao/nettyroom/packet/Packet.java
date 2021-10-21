package com.lingbao.nettyroom.packet;

import com.lingbao.nettyroom.anno.PacketCmd;
import com.lingbao.nettyroom.constant.CommandType;
import lombok.Data;

/**
 * @author lingbao08
 * @DESCRIPTION
 * @create 2019-02-22 18:07
 **/

@Data
public abstract class Packet {
    /**
     * 协议版本
     */
    private Byte version = 1;

    /**
     * 指令
     *
     * @return
     */
//    public abstract Byte getCommand();
    public Byte getCommand() {
        PacketCmd packetCmd = this.getClass().getAnnotation(PacketCmd.class);
        if (packetCmd != null) {
            CommandType commandType = packetCmd.command();
            return commandType.getValue();
        }
        //TODO 这里优化为一个异常
        return null;
    }
}
