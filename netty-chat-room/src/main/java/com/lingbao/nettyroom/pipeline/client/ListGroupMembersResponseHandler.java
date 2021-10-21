package com.lingbao.nettyroom.pipeline.client;

import com.lingbao.nettyroom.entity.Member;
import com.lingbao.nettyroom.packet.resp.ListGroupMembersResponsePacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.stream.Collectors;

/**
 * @author lingbao08
 * @DESCRIPTION
 * @create 2019-02-24 18:22
 **/

@Slf4j
public class ListGroupMembersResponseHandler extends SimpleChannelInboundHandler<ListGroupMembersResponsePacket> {


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ListGroupMembersResponsePacket msg) throws Exception {

        if (StringUtils.isNotEmpty(msg.getMessage())) {

            System.out.println(msg.getMessage());
        } else {
            System.out.println("群成员有：" +
                    msg.getMembers().parallelStream().map(Member::getUserName)
                            .collect(Collectors.joining(",")));
        }

    }
}
