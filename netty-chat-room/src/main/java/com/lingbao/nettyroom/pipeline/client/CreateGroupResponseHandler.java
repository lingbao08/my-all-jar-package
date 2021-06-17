package com.lingbao.nettyroom.pipeline.client;

import com.lingbao.nettyroom.entity.CreateGroupResponsePacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author lingbao08
 * @DESCRIPTION
 * @create 2019-02-24 17:11
 **/

@Slf4j
public class CreateGroupResponseHandler extends SimpleChannelInboundHandler<CreateGroupResponsePacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, CreateGroupResponsePacket msg) throws Exception {

        //这条消息会在client端进行打印
        log.info("群创建成功，id 为[{}], 群里面有：{}", msg.getGroupId(), msg.getUserNameList());
    }
}
