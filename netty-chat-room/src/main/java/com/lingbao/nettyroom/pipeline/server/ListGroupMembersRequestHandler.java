package com.lingbao.nettyroom.pipeline.server;

import com.lingbao.nettyroom.constant.Attributes;
import com.lingbao.nettyroom.entity.Member;
import com.lingbao.nettyroom.packet.request.ListGroupMembersRequestPacket;
import com.lingbao.nettyroom.packet.resp.ListGroupMembersResponsePacket;
import com.lingbao.nettyroom.pkg.login.MySessionUtil;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;

import static com.lingbao.nettyroom.constant.Attributes.currentGroupId;

/**
 * @author lingbao08
 * @desc
 * @date 10/21/21 21:07
 **/
@Slf4j
@ChannelHandler.Sharable
public class ListGroupMembersRequestHandler
        extends SimpleChannelInboundHandler<ListGroupMembersRequestPacket> {

    public static final ListGroupMembersRequestHandler INSTANCE = new ListGroupMembersRequestHandler();

    private ListGroupMembersRequestHandler() {
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ListGroupMembersRequestPacket msg)
            throws Exception {

        ChannelGroup channelGroup =
                MySessionUtil.getChannelGroup(ctx.channel().attr(currentGroupId).get());

        ListGroupMembersResponsePacket packet = new ListGroupMembersResponsePacket();
        if (channelGroup == null) {
            packet.setMessage("当前群组不存在");

        } else {

            // 根据channelGroup获取其中所有的channel，然后遍历其中的session属性，取出用户属性
            List<Member> members = channelGroup.parallelStream()
                    .map(x -> x.attr(Attributes.SESSION).get().getMember())
                    .collect(Collectors.toList());

            packet.setMembers(members);

        }
        // 将数据返回给客户端
        ctx.writeAndFlush(packet);
    }
}
