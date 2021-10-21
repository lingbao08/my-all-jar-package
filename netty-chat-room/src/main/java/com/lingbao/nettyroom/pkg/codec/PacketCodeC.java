package com.lingbao.nettyroom.pkg.codec;


import com.lingbao.nettyroom.constant.CommandType;
import com.lingbao.nettyroom.packet.Packet;
import com.lingbao.nettyroom.pkg.JSONSerializer;
import com.lingbao.nettyroom.pkg.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

import java.util.HashMap;
import java.util.Map;


/**
 * @author lingbao08
 * @DESCRIPTION
 * @create 2019-02-22 18:25
 **/

public class PacketCodeC {

    public static final int MAGIC_NUMBER = 0x12345678;

    public static PacketCodeC INSTANCE = new PacketCodeC();

    private final Map<Byte, Class<? extends Packet>> packetTypeMap;
    private final Map<Byte, Serializer> serializerMap;


    private PacketCodeC() {
        packetTypeMap = CommandType.buildPacketMap();

        serializerMap = new HashMap<>();
        Serializer serializer = new JSONSerializer();
        serializerMap.put(serializer.getSerializerAlgorithm(), serializer);
    }

    public ByteBuf encode(ByteBuf byteBuf, Packet packet) {
        // 1. 创建 ByteBuf 对象
        ByteBufAllocator.DEFAULT.buffer();
        // 2. 序列化 Java 对象
        byte[] bytes = Serializer.DEFAULT.serialize(packet);

        // 3. 实际编码过程
        byteBuf.writeInt(MAGIC_NUMBER)
                .writeByte(packet.getVersion())
                .writeByte(Serializer.DEFAULT.getSerializerAlgorithm())
                .writeByte(packet.getCommand())
                .writeInt(bytes.length)
                .writeBytes(bytes);

        return byteBuf;
    }

    public Packet decode(ByteBuf byteBuf) {
        // 跳过 magic number
        byteBuf.skipBytes(4);
        // 跳过版本号
        byteBuf.skipBytes(1);

        // 序列化算法标识
        byte serializeAlgorithm = byteBuf.readByte();
        // 指令
        byte command = byteBuf.readByte();

        // 数据包长度
        int length = byteBuf.readInt();

        byte[] bytes = new byte[length];
        byteBuf.readBytes(bytes);

        Class<? extends Packet> requestType = getRequestType(command);
        Serializer serializer = getSerializer(serializeAlgorithm);

        if (requestType != null && serializer != null) {
            return serializer.deserialize(requestType, bytes);
        }

        return null;
    }

    private Serializer getSerializer(byte serializeAlgorithm) {
        //默认为JSON算法
        return Serializer.DEFAULT;
    }

    private Class<? extends Packet> getRequestType(byte command) {

        return packetTypeMap.get(command);
    }
}
