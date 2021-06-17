package com.lingbao.nettyroom.pipeline.client;


import com.lingbao.nettyroom.entity.MessageResponsePacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author lingbao08
 * @DESCRIPTION
 * @create 2019-02-23 19:58
 **/

public class MessageResponseHandler extends SimpleChannelInboundHandler<MessageResponsePacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageResponsePacket msg) throws Exception {

        String fromUserId = msg.getFromUserId();
        String fromUserName = msg.getFromUserName();
        System.out.println(fromUserId + " " + fromUserName + ":  " + msg.getMsg());

        //因为在服务端没有handler处理本次写入的数据，该数据将会被丢弃
//        ctx.pipeline().writeAndFlush("aa");
    }
}
