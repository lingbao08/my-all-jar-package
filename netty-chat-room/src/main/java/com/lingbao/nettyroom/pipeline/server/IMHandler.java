package com.lingbao.nettyroom.pipeline.server;

import com.lingbao.nettyroom.entity.Packet;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.HashMap;
import java.util.Map;

import static com.lingbao.nettyroom.entity.Command.*;

/**
 * @author lingbao08
 * @DESCRIPTION
 * @create 2019-02-24 20:03
 **/
@ChannelHandler.Sharable
public class IMHandler extends SimpleChannelInboundHandler<Packet> {

    public static final IMHandler INSTANCE = new IMHandler();

    private Map<Byte, SimpleChannelInboundHandler<? extends Packet>> handlerMap;

    private IMHandler() {
        handlerMap = new HashMap<>();

        handlerMap.put(MESSAGE_REQUEST, MessageRequestHandler.INSTANCE);
        handlerMap.put(CREATE_GROUP_REQ, CreateGroupRequestHandler.INSTANCE);
        handlerMap.put(ADD_TO_GROUP_REQ, AddGroupRequestHandler.INSTANCE);
//        handlerMap.put(LIST_GROUP_REQ, ListGroupMembersRequestHandler.INSTANCE);
        handlerMap.put(MESSAGE_REQUEST, MessageRequestHandler.INSTANCE);
        handlerMap.put(LOGOUT_GROUP_REQ, LoginRequestHandler.INSTANCE);
    }


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Packet msg) throws Exception {
        handlerMap.get(msg.getCommand()).channelRead(ctx, msg);
    }
}
