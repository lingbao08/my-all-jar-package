package com.lingbao.nettyUpload.client;

import com.lingbao.nettyUpload.config.FileUploadProperties;
import com.lingbao.nettyUpload.module.FileUploadFile;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author lingbao08
 * @DESCRIPTION
 * @create 2019-02-18 22:54
 **/
@Component
public class FileUploadClientComponent {

//    @Value("${file.upload.host:127.0.0.1}")
//    private String host;
//
//    @Value("${file.upload.port:8080}")
//    private int port;

    @Autowired
    private FileUploadProperties properties;

    /**
     * @param fileUploadFile 要上传的文件对象
     * @throws Exception
     */
    public void connect(final FileUploadFile fileUploadFile) {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group).channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<Channel>() {

                        @Override
                        protected void initChannel(Channel ch) throws Exception {
                            ch.pipeline().addLast(new ObjectEncoder())
                                    .addLast(new ObjectDecoder(
                                            ClassResolvers.weakCachingConcurrentResolver(
                                                    this.getClass().getClassLoader())))
                                    .addLast(new FileUploadClientHandler(fileUploadFile));
                        }
                    });
            ChannelFuture f = b.connect(properties.getHost(), properties.getPort()).sync();
            f.channel().closeFuture().sync();
        } catch (Exception e) {
            throw new IllegalArgumentException("上传文件错误", e);
        } finally {
            group.shutdownGracefully();
        }
    }
}
