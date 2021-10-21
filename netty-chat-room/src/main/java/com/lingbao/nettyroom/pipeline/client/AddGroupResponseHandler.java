package com.lingbao.nettyroom.pipeline.client;

import com.lingbao.nettyroom.constant.Attributes;
import com.lingbao.nettyroom.packet.resp.AddGroupResponsePacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.springframework.util.StringUtils;

/**
 * @author lingbao08
 * @DESCRIPTION
 * @create 2019-02-24 18:22
 **/

public class AddGroupResponseHandler extends SimpleChannelInboundHandler<AddGroupResponsePacket> {


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, AddGroupResponsePacket msg) throws Exception {
        boolean success = msg.isSuccess();
        if (success) {
            Integer addUserId = msg.getAddMember().getUserId();
            if (ctx.channel().hasAttr(Attributes.userId)) {
                Integer userId = ctx.channel().attr(Attributes.userId).get();
                //如果用户ID不为空，且等于自己的ID，那么只写您已加入XXX群聊
                if (!StringUtils.isEmpty(userId) && userId.equals(addUserId)) {
                    System.out.println("您已加入群[" + msg.getGroupId() + "]!");
                } else {
                    String addUserName = msg.getAddMember().getUserName();
                    System.out.println(addUserName + "已加入群[" + msg.getGroupId() + "]!");
                }
            }else{
                System.out.println("登录异常！！");
            }
        } else {
            System.err.println("加入群[" + msg.getGroupId() + "]失败，原因为：" + msg.getReason());
        }
    }
}
