package com.lingbao.nettyroom.pipeline.server;

import com.lingbao.nettyroom.entity.AddGroupRequestPacket;
import com.lingbao.nettyroom.entity.AddGroupResponsePacket;
import com.lingbao.nettyroom.pkg.login.MySession;
import com.lingbao.nettyroom.pkg.login.MySessionUtil;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;

/**
 * @author lingbao08
 * @DESCRIPTION
 * @create 2019-02-24 18:07
 **/
@ChannelHandler.Sharable
public class AddGroupRequestHandler extends SimpleChannelInboundHandler<AddGroupRequestPacket> {

    public static final AddGroupRequestHandler INSTANCE = new AddGroupRequestHandler();

    private AddGroupRequestHandler(){}

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, AddGroupRequestPacket msg) throws Exception {
        //获取组ID
        String groupId = msg.getGroupId();
        //如果组信息不存在，说不存在
        //如果组信息存在，则添加用户所在的channel到该channelGroup中去
        ChannelGroup channelGroup = MySessionUtil.getChannelGroup(groupId);
        AddGroupResponsePacket addGroupResponsePacket = new AddGroupResponsePacket();
        if (channelGroup == null) {
            addGroupResponsePacket.setSuccess(false);
            addGroupResponsePacket.setReason("该组不存在或已经解散！");
            ctx.channel().writeAndFlush(addGroupResponsePacket);
        } else {
            //添加用户所在的channel到该channelGroup中去
            channelGroup.add(ctx.channel());
            //构建响应信息
            addGroupResponsePacket.setGroupId(groupId);
            addGroupResponsePacket.setSuccess(true);
            MySession session = MySessionUtil.getSession(ctx.channel());
            addGroupResponsePacket.setAddUserId(session.getUserId());
            addGroupResponsePacket.setAddUserName(session.getUserName());
            //给群里的其他成员说 "XXX加入了群中"
            channelGroup.writeAndFlush(addGroupResponsePacket);
        }

    }
}
