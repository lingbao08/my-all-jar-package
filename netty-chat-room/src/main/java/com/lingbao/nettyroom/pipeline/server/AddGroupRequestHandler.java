package com.lingbao.nettyroom.pipeline.server;

import com.lingbao.nettyroom.packet.request.AddGroupRequestPacket;
import com.lingbao.nettyroom.packet.resp.AddGroupResponsePacket;
import com.lingbao.nettyroom.pkg.login.MySession;
import com.lingbao.nettyroom.pkg.login.MySessionUtil;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;

import static com.lingbao.nettyroom.constant.Attributes.currentGroupId;

/**
 * @author lingbao08
 * @DESCRIPTION
 * @create 2019-02-24 18:07
 **/

@ChannelHandler.Sharable
public class AddGroupRequestHandler extends SimpleChannelInboundHandler<AddGroupRequestPacket> {

    public static final AddGroupRequestHandler INSTANCE = new AddGroupRequestHandler();

    private AddGroupRequestHandler() {
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, AddGroupRequestPacket msg) throws Exception {
        //获取组ID
        String groupId = msg.getGroupId();
        //如果组信息不存在，说不存在
        //如果组信息存在，则添加用户所在的channel到该channelGroup中去
        ChannelGroup channelGroup = MySessionUtil.getChannelGroup(groupId);

        if (channelGroup == null) {
            //如群组为空，或者群组不存在，添加失败
            ctx.channel().writeAndFlush(
                    AddGroupResponsePacket.builder()
                            .success(false).reason("该组不存在或已经解散！")
                            .build());

        } else {

            //添加用户所在的channel到该channelGroup中去
            channelGroup.add(ctx.channel());

            //将当前群组ID写入到channel中，在遍历等地方取出
            ctx.channel().attr(currentGroupId).set(groupId);

            //构建响应信息
            MySession session = MySessionUtil.getSession(ctx.channel());
            //给群里的其他成员说 "XXX加入了群中"
            channelGroup.writeAndFlush(
                    AddGroupResponsePacket.builder()
                            .groupId(groupId).success(true).addMember(session.getMember())
                            .reason("XXX加入了群中")
                            .build());
        }

    }
}
