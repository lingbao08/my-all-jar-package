package com.lingbao.nettyroom.entity;

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
    public abstract Byte getCommand();
}
