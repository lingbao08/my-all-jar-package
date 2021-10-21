package com.lingbao.nettyroom.constant;

import com.lingbao.nettyroom.packet.Packet;
import com.lingbao.nettyroom.packet.request.*;
import com.lingbao.nettyroom.packet.resp.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public enum CommandType {


    COMMON_RES("", (byte) 0, null),
    LOGIN_REQUEST("", (byte) 1, LoginRequestPacket.class),
    LOGIN_RESPONSE("", (byte) 2, LoginResponsePacket.class),
    MESSAGE_REQUEST("", (byte) 3, MessageRequestPacket.class),
    MESSAGE_RESPONSE("", (byte) 4, MessageResponsePacket.class),
    CREATE_GROUP_REQ("", (byte) 5, CreateGroupRequestPacket.class),
    CREATE_GROUP_RES("", (byte) 6, CreateGroupResponsePacket.class),
    SEND_GROUP_REQ("", (byte) 7, SendGroupRequestPacket.class),
    SEND_GROUP_RES("", (byte) 8, SendGroupResponsePacket.class),
    LOGOUT_GROUP_REQ("", (byte) 9, null),
    LOGOUT_GROUP_RES("", (byte) 10, null),
    LIST_GROUP_REQ("", (byte) 11, ListGroupMembersRequestPacket.class),
    LIST_GROUP_RES("", (byte) 12, ListGroupMembersResponsePacket.class),
    ADD_TO_GROUP_REQ("", (byte) 13, AddGroupRequestPacket.class),
    ADD_TO_GROUP_RES("", (byte) 14, AddGroupResponsePacket.class),


    //
    ;

    private String msg;

    private Byte value;

    private Class<? extends Packet> packet;

    public static Map<Byte, Class<? extends Packet>> buildPacketMap() {
        return Arrays.stream(values())
                .filter(x -> x.packet != null)
                .collect(Collectors.toMap(CommandType::getValue, CommandType::getPacket, (x1, x2) -> x1));
    }

}
