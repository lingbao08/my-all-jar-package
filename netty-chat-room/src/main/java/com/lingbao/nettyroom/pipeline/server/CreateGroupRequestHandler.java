package com.lingbao.nettyroom.pipeline.server;

import com.lingbao.nettyroom.entity.CreateGroupRequestPacket;
import com.lingbao.nettyroom.entity.CreateGroupResponsePacket;
import com.lingbao.nettyroom.pkg.login.MySessionUtil;
import com.lingbao.nettyroom.utils.IDUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.DefaultChannelGroup;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lingbao08
 * @DESCRIPTION
 * @create 2019-02-24 16:50
 **/

@Slf4j
@ChannelHandler.Sharable
public class CreateGroupRequestHandler extends SimpleChannelInboundHandler<CreateGroupRequestPacket> {

    public static final CreateGroupRequestHandler INSTANCE = new CreateGroupRequestHandler();

    private CreateGroupRequestHandler() {
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, CreateGroupRequestPacket msg) throws Exception {
        //1. 先获取到所有的userId
        List<String> userIdList = msg.getUserIdList();
        //2. 创建 channel 分组
        DefaultChannelGroup channelGroup = new DefaultChannelGroup(ctx.executor());
        //3. 将所有的用户名放入到一个新的list中
        //同时将用户的channel放到上面的channelGroup中
        List<String> nameList = new ArrayList<>();
        for (String userId : userIdList) {
            //从session中获取一下，查看该id是否正确，用户是否登录，对于不正确的不作处理
            Channel channel = MySessionUtil.getChannel(userId);
            if (channel != null) {
                channelGroup.add(channel);
                nameList.add(MySessionUtil.getSession(channel).getUserName());
            }
        }

        //4. 拉人完毕后，组装响应
        CreateGroupResponsePacket groupResponsePacket = new CreateGroupResponsePacket();
        groupResponsePacket.setSuccess(true);
        String groupId = IDUtil.getGroupId();
        groupResponsePacket.setGroupId(groupId);
        groupResponsePacket.setUserNameList(nameList);

        //3.1 将该群组写入缓存中
        MySessionUtil.addChannelGroup(groupId, channelGroup);


        //5. 给每个用户发送拉群通知--建群完毕！！
        channelGroup.writeAndFlush(groupResponsePacket);

        //这条信息会在服务端进行打印
        log.info("群创建成功，id 为[{}],群里面有：{}",
                groupResponsePacket.getGroupId(), groupResponsePacket.getUserNameList());
    }
}
