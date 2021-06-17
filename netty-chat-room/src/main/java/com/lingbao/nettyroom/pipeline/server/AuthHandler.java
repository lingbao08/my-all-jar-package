package com.lingbao.nettyroom.pipeline.server;


import com.lingbao.nettyroom.pkg.login.MySessionUtil;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author lingbao08
 * @DESCRIPTION
 * @create 2019-02-24 14:47
 **/
@ChannelHandler.Sharable
public class AuthHandler extends ChannelInboundHandlerAdapter {

    public static final AuthHandler INSTANCE = new AuthHandler();
private AuthHandler(){}

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //用户正确
        if (MySessionUtil.hasLogin(ctx.channel())) {
            ctx.pipeline().remove(this);
            super.channelRead(ctx, msg);
        } else {
            System.out.println("该用户没有登录！！！！");
            //用户错误
            ctx.channel().close();
        }
    }

//    @Override
//    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
//        if (LoginUtil.hasLogin(ctx.channel())) {
////            System.out.println("权限验证通过！！！，移除自己开始执行！");
//            super.handlerRemoved(ctx);
//        } else {
////            System.out.println("无登录权限，连接关闭！！");
//        }
//    }
}
