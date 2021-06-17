package com.lingbao.nettyroom.pipeline.client;


import com.lingbao.nettyroom.entity.LoginResponsePacket;
import com.lingbao.nettyroom.pkg.login.MySession;
import com.lingbao.nettyroom.pkg.login.MySessionUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.AttributeKey;

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
//            LoginUtil.markAsLogin(ctx.channel());
            MySession mySession = new MySession(msg.getUserId(), msg.getUserName());
            MySessionUtil.bindSession(mySession, ctx.channel());
            ctx.channel().attr(AttributeKey.valueOf("userId")).set(mySession.getUserId());
            System.out.println(
                    Instant.now() + "【" + mySession.getUserId() + " - "
                            + mySession.getUserName() + "】" + ":客户端登录成功!");
        } else {
            System.out.println(Instant.now() + msg.getCause());
        }
        //因为在服务端没有handler处理本次写入的数据，该数据将会被丢弃
//        ctx.pipeline().writeAndFlush(msg);
    }


}
