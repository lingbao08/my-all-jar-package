package com.lingbao.nettyroom.pipeline;


import com.lingbao.nettyroom.pkg.PacketCodeC;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 这是校验协议最前面的魔数的，如果不是对应的数字，则直接丢弃该消息。
 *
 * @author lingbao08
 * @DESCRIPTION
 * @create 2019-02-24 14:04
 **/

@Slf4j
public class MoShuChannelHandler extends LengthFieldBasedFrameDecoder {


    private volatile AtomicInteger i = new AtomicInteger();

    public MoShuChannelHandler() {
        super(Integer.MAX_VALUE, 7, 4);
    }

    public MoShuChannelHandler(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength) {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength);
    }


    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {

        if (in.getInt(in.readerIndex()) != PacketCodeC.MAGIC_NUMBER) {
            //如果魔数不匹配，则直接关闭channel
            ctx.channel().close();
            return null;
        }
        return super.decode(ctx, in);
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        log.info("当前客户端预备连接，连接数为：{}", i.incrementAndGet());
        super.channelRegistered(ctx);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        log.info("当前客户端连接失败，此时连接数为：{}", i.decrementAndGet());

        super.channelUnregistered(ctx);
    }
}
