package com.lingbao.nettyroom.pipeline.server;

import com.lingbao.nettyroom.constant.CommandType;
import com.lingbao.nettyroom.packet.Packet;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;


/**
 * 在{@link com.lingbao.nettyroom.start.ServerNetty}中添加了这个类后，
 * {@link IMHandler}中的handlerMap所配置的所有的requestHandler，都不用去ServerNetty中进行添加了。
 * 相当于是一个智能分流器。
 *
 * @author lingbao08
 * @DESCRIPTION
 * @create 2019-02-24 20:03
 **/
@Slf4j
@ChannelHandler.Sharable
public class IMHandler extends SimpleChannelInboundHandler<Packet> {

    public static final IMHandler INSTANCE = new IMHandler();

    private Map<Byte, SimpleChannelInboundHandler<? extends Packet>> handlerMap;

    private IMHandler() {
        handlerMap = new HashMap<>();

        handlerMap.put(CommandType.MESSAGE_REQUEST.getValue(), MessageRequestHandler.INSTANCE);
        handlerMap.put(CommandType.CREATE_GROUP_REQ.getValue(), CreateGroupRequestHandler.INSTANCE);
        handlerMap.put(CommandType.ADD_TO_GROUP_REQ.getValue(), AddGroupRequestHandler.INSTANCE);
        handlerMap.put(CommandType.LIST_GROUP_REQ.getValue(), ListGroupMembersRequestHandler.INSTANCE);
        handlerMap.put(CommandType.LOGOUT_GROUP_REQ.getValue(), LoginRequestHandler.INSTANCE);

    }


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Packet msg) throws Exception {
        SimpleChannelInboundHandler<? extends Packet> handler = handlerMap.get(msg.getCommand());
        if (handler == null) {
            log.info("这个类型的requestHandler没有：{}", msg.getCommand());
            return;
        }
        handler.channelRead(ctx, msg);
    }
}
