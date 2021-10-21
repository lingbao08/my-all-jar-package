package com.lingbao.nettyroom.pipeline.server;


import com.lingbao.nettyroom.constant.CommandType;
import com.lingbao.nettyroom.entity.Member;
import com.lingbao.nettyroom.packet.request.LoginRequestPacket;
import com.lingbao.nettyroom.packet.resp.LoginResponsePacket;
import com.lingbao.nettyroom.pkg.login.LoginUtil;
import com.lingbao.nettyroom.pkg.login.MySession;
import com.lingbao.nettyroom.pkg.login.MySessionUtil;
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
            String username = msg.getUsername();

            //服务器针对于用户的某一条连接生成自己对于该客户的userId，然后将该userId和userName都存到HashMap中
            MySessionUtil.bindSession(
                    MySession.builder().member(
                            Member.builder().userId(msg.getUserId()).userName(username).build()).build(),
                    ctx.channel());
            System.out.println("【" + username + "】已经登录了！！！");
        }
        //因为有packetEncoder类实现了消息的编码
        ctx.pipeline().writeAndFlush(loginResponsePacket);

    }

    /**
     * 登录信息处理
     * <p>
     * 标记登录，记住用户名，
     *
     * @param ctx
     * @param packet
     * @return
     */
    private LoginResponsePacket decodeLoginRequest(ChannelHandlerContext ctx, LoginRequestPacket packet) {
        LoginResponsePacket responsePacket = new LoginResponsePacket();
        responsePacket.setSuccess(false);
        Byte command = packet.getCommand();

        if (packet.getUsername().endsWith("2")) {
            responsePacket.setCause("用户名中有非法字符！");
            return responsePacket;
        }

        if (command.equals(CommandType.LOGIN_REQUEST.getValue())) {
            Integer userId = packet.getUserId();
            if (userId > 10000) {
                responsePacket.setCause("用户ID信息不正确！");
                return responsePacket;
            }
            //登录成功，记住用户的连接
            LoginUtil.markAsLogin(ctx.channel());
            responsePacket.setSuccess(true);
            responsePacket.setUserId(userId);
            responsePacket.setUserName(packet.getUsername());
        } else {
            responsePacket.setCause("标志位不正确！");
        }
        return responsePacket;
    }


}
