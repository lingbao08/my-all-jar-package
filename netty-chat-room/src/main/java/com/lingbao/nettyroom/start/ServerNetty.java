package com.lingbao.nettyroom.start;


import com.lingbao.nettyroom.pipeline.MoShuChannelHandler;
import com.lingbao.nettyroom.pipeline.server.*;
import com.lingbao.nettyroom.pipeline.PacketCodecHandler;
import com.lingbao.nettyroom.pkg.codec.PacketEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author lingbao08
 * @DESCRIPTION
 * @create 2019-02-22 19:02
 **/

public class ServerNetty {

    public static void create() throws InterruptedException {
        ServerBootstrap bootstrap = new ServerBootstrap();

        connect(bootstrap, 8001);
    }

    public static void connect(ServerBootstrap bootstrap, int port) throws InterruptedException {
        NioEventLoopGroup boss = new NioEventLoopGroup();
        NioEventLoopGroup worker = new NioEventLoopGroup();


        bootstrap.group(boss, worker)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel ch) throws Exception {
                        //魔数校验，即对我们封装的协议进行校验
                        ch.pipeline()
                                .addLast(new MoShuChannelHandler())
                                //1. 传入消息解码
                                .addLast(PacketCodecHandler.INSYANCE)

                                //2. 添加登录请求的handler
                                .addLast(LoginRequestHandler.INSTANCE)
                                //添加权限校验的处理器
                                .addLast(AuthHandler.INSTANCE)

                                //将下面所有的指令handler合并到一个中
                                //所有的requestHandler指令处理
                                .addLast(IMHandler.INSTANCE)
                                //3. 添加消息的处理
                                .addLast(MessageRequestHandler.INSTANCE)
                                // 4. 传出消息编码
                                .addLast(new PacketEncoder());
                    }
                });

        ChannelFuture sync = bootstrap.bind("127.0.0.1", port)
                .addListener(future -> {
                    if (future.isSuccess()) {
                        System.out.println("端口[" + port + "]绑定成功!");
                    } else {
                        System.err.println("端口[" + port + "]绑定失败!");
                        connect(bootstrap, port + 1);
                    }
                }).sync();

    }
}
