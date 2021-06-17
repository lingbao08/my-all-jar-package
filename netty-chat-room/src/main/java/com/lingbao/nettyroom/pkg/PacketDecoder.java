package com.lingbao.nettyroom.pkg;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @author lingbao08
 * @DESCRIPTION
 * @create 2019-02-22 21:57
 **/

public class PacketDecoder extends ByteToMessageDecoder {

    //当我们继承了 ByteToMessageDecoder 这个类之后，我们只需要实现一下 decode() 方法，
    // 这里的 in 大家可以看到，传递进来的时候就已经是 ByteBuf 类型，所以我们不再需要强转，
    // 第三个参数是 List 类型，我们通过往这个 List 里面添加解码后的结果对象，
    // 就可以自动实现结果往下一个 handler 进行传递，这样，我们就实现了解码的逻辑 handler

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        out.add(PacketCodeC.INSTANCE.decode(in));
    }
}
