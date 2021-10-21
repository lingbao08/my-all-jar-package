package com.lingbao.nettyroom.pipeline.client;


import com.lingbao.nettyroom.constant.Attributes;
import com.lingbao.nettyroom.entity.Member;
import com.lingbao.nettyroom.packet.resp.LoginResponsePacket;
import com.lingbao.nettyroom.pkg.login.LoginUtil;
import com.lingbao.nettyroom.pkg.login.MySession;
import com.lingbao.nettyroom.pkg.login.MySessionUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.time.Instant;

/**
 * @author lingbao08
 * @DESCRIPTION
 * @create 2019-02-23 19:57
 **/

public class LoginResponseHandler extends SimpleChannelInboundHandler<LoginResponsePacket> {


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginResponsePacket msg) throws Exception {
        if (msg.isSuccess()) {
            //标记该channel为已经登陆了
            LoginUtil.markAsLogin(ctx.channel());

            Integer userId = msg.getUserId();
            String userName = msg.getUserName();

            MySessionUtil.bindSession(
                    MySession.builder().member(
                            Member.builder().userId(userId).userName(userName).build()).build(), ctx.channel());

            // 将mySession中的用户ID赋值给channel中的userId
            ctx.channel().attr(Attributes.userId).set(userId);
            System.out.println(
                    Instant.now() + "【" + userId + " - " + userName + "】" + ":客户端登录成功!");
        } else {
            System.out.println(Instant.now() + msg.getCause());
        }
        //返回给服务端，让服务端提示下一步操作
        ctx.pipeline().writeAndFlush(msg);
    }


}
