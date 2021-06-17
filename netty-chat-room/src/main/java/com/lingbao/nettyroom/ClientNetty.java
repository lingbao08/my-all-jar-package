package com.lingbao.nettyroom;

import com.lingbao.nettyroom.entity.LoginConsoleCommand;
import com.lingbao.nettyroom.pipeline.MoShuChannelHandler;
import com.lingbao.nettyroom.pipeline.client.AddGroupResponseHandler;
import com.lingbao.nettyroom.pipeline.client.CreateGroupResponseHandler;
import com.lingbao.nettyroom.pipeline.client.LoginResponseHandler;
import com.lingbao.nettyroom.pipeline.client.MessageResponseHandler;
import com.lingbao.nettyroom.pkg.PacketDecoder;
import com.lingbao.nettyroom.pkg.PacketEncoder;
import com.lingbao.nettyroom.pkg.cmd.ConsoleCommandManager;
import com.lingbao.nettyroom.pkg.login.MySessionUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/**
 * @author lingbao08
 * @DESCRIPTION
 * @create 2019-02-22 18:52
 **/

@Slf4j
public class ClientNetty {

    public static final int MAX_RETRY = 5;

    public static void create() {
        Bootstrap bootstrap = new Bootstrap();
        new ClientNetty().connect(bootstrap, 8001);
    }

    public void connect(Bootstrap bootstrap, int port) {
        NioEventLoopGroup group = new NioEventLoopGroup();

        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel ch) throws Exception {
                        //校验协议是否是我们的协议
                        ch.pipeline().addLast(new MoShuChannelHandler())
                                //解码
                                .addLast(new PacketDecoder())
                                //登录处理
                                .addLast(new LoginResponseHandler())
                                //创建组
                                .addLast(new CreateGroupResponseHandler())
                                //添加组
                                .addLast(new AddGroupResponseHandler())
                                //发送消息处理
                                .addLast(new MessageResponseHandler())
                                //编码
                                .addLast(new PacketEncoder());
                    }
                });


        connect(bootstrap, "127.0.0.1", port, 0);
    }

    /**
     * 连接方法，失败后会重连，重连 MAX_RETRY 次后，退出
     *
     * @param bootstrap
     * @param host
     * @param port
     * @param retry
     */
    private void connect(Bootstrap bootstrap, final String host, int port, final int retry) {
        bootstrap.connect(host, port)
                .addListener(future -> {
                    Channel channel = ((ChannelFuture) future).channel();
                    if (future.isSuccess()) {
                        log.info("连接成功");
                        startConsoleThread(channel);
                    } else {
                        int retryTimes = retry + 1;
                        log.error("连接失败！进行第{}次重连...", retryTimes);
                        if (retryTimes > MAX_RETRY) {
                            log.error("重连次数过多，断开连接");
                            System.exit(1);
                        }
                        bootstrap.config().group().schedule(() -> {
                            connect(bootstrap, host, port, retryTimes);
                        }, 1 << retryTimes, TimeUnit.SECONDS);
                    }
                });
    }


    /**
     * 命令行方法
     *
     * @param channel
     */
    private void startConsoleThread(Channel channel) {
        Scanner sc = new Scanner(System.in);
        ConsoleCommandManager consoleCommandManager = new ConsoleCommandManager();
        LoginConsoleCommand loginConsoleCommand = new LoginConsoleCommand();

        new Thread(() -> {
            while (!Thread.interrupted()) {
                if (!MySessionUtil.hasLogin(channel)) {
                    //用户未登录的情况
                    loginConsoleCommand.exec(sc, channel);
                } else {
                    //登录之后发送消息
                    consoleCommandManager.exec(sc, channel);
                }
            }
        }).start();
    }


}
