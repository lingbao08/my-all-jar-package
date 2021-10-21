package com.lingbao.nettyroom.pipeline.server;


import com.lingbao.nettyroom.packet.request.MessageRequestPacket;
import com.lingbao.nettyroom.packet.resp.MessageResponsePacket;
import com.lingbao.nettyroom.pkg.login.MySession;
import com.lingbao.nettyroom.pkg.login.MySessionUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author lingbao08
 * @DESCRIPTION
 * @create 2019-02-23 18:58
 **/
@ChannelHandler.Sharable
public class MessageRequestHandler extends SimpleChannelInboundHandler<MessageRequestPacket> {

    public static final MessageRequestHandler INSTANCE = new MessageRequestHandler();

    private MessageRequestHandler() {
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageRequestPacket msg) throws Exception {
        //1. 首先，我们要知道是谁发过来的
        MySession session = MySessionUtil.getSession(ctx.channel());
        //2. 构建出该消息在接收方要显示的名字和消息内容
        MessageResponsePacket messageResponsePacket = new MessageResponsePacket();
        messageResponsePacket.setMsg(msg.getMsg());
        messageResponsePacket.setFromMember(session.getMember());

        //3. 拿到消息接收方的channel
        Channel toChannel = MySessionUtil.getChannel(msg.getToUserId());

        //4. 校验该channel不为空，且该channel的用户是登录的
        if (toChannel != null) {
            if (MySessionUtil.hasLogin(toChannel)) {
                toChannel.writeAndFlush(messageResponsePacket);
            } else {
                MySession toSession = MySessionUtil.getSession(toChannel);
                messageResponsePacket = new MessageResponsePacket();
                messageResponsePacket.setMsg("【" + toSession.getMember().getUserName() + "】用户已离线！！发送失败！");
                ctx.channel().writeAndFlush(messageResponsePacket);
            }
        }
    }
}
