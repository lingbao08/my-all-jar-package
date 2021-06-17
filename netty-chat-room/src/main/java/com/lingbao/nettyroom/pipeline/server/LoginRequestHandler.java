package com.lingbao.nettyroom.pipeline.server;


import com.lingbao.nettyroom.entity.Command;
import com.lingbao.nettyroom.entity.LoginRequestPacket;
import com.lingbao.nettyroom.entity.LoginResponsePacket;
import com.lingbao.nettyroom.pkg.login.LoginUtil;
import com.lingbao.nettyroom.pkg.login.MySession;
import com.lingbao.nettyroom.pkg.login.MySessionUtil;
import com.lingbao.nettyroom.utils.IDUtil;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author lingbao08
 * @DESCRIPTION
 * @create 2019-02-22 22:01
 **/

@ChannelHandler.Sharable
public class LoginRequestHandler extends SimpleChannelInboundHandler<LoginRequestPacket> {


    public static final LoginRequestHandler INSTANCE = new LoginRequestHandler();

    private LoginRequestHandler() {
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginRequestPacket msg) throws Exception {

        //解析登录
        LoginResponsePacket loginResponsePacket = decodeLoginRequest(ctx, msg);
        if (loginResponsePacket.isSuccess()) {
            String userId = IDUtil.getUserId();
            loginResponsePacket.setUserId(userId);
            //服务器针对于用户的某一条连接生成自己对于该客户的userId，然后将该userId和userName都存到HashMap中
            MySession session = new MySession(userId, msg.getUsername());
            MySessionUtil.bindSession(session, ctx.channel());
            System.out.println("【" + session.getUserName() + "】已经登录了！！！");
        }
        //因为有packetEncoder类实现了消息的编码
        ctx.pipeline().writeAndFlush(loginResponsePacket);

    }

    private LoginResponsePacket decodeLoginRequest(ChannelHandlerContext ctx, LoginRequestPacket packet) {
        LoginResponsePacket responsePacket = new LoginResponsePacket();
        responsePacket.setSuccess(false);
        Byte command = packet.getCommand();

        if (packet.getUsername().endsWith("2")) {
            responsePacket.setCause("用户名中有非法字符！");
            return responsePacket;
        }

        if (command.equals(Command.LOGIN_REQUEST)) {
            Integer userId = packet.getUserId();
            if (userId > 10000) {
                responsePacket.setCause("用户ID信息不正确！");
                return responsePacket;
            }
            //登录成功，记住用户的连接
            LoginUtil.markAsLogin(ctx.channel());
            responsePacket.setSuccess(true);
            responsePacket.setUserName(packet.getUsername());
        } else {
            responsePacket.setCause("标志位不正确！");
        }
        return responsePacket;
    }


}
