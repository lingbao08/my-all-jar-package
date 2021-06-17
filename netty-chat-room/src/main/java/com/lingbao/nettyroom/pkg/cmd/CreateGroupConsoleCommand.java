package com.lingbao.nettyroom.pkg.cmd;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.lingbao.nettyroom.entity.CreateGroupRequestPacket;
import com.lingbao.nettyroom.pkg.login.MySession;
import com.lingbao.nettyroom.pkg.login.MySessionUtil;
import io.netty.channel.Channel;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Scanner;
import java.util.Set;

/**
 * @author lingbao08
 * @DESCRIPTION
 * @create 2019-02-24 16:34
 **/

public class CreateGroupConsoleCommand implements ConsoleCommand {

    private static final String USER_ID_SPLITER = ",";

    @Override
    public void exec(Scanner scanner, Channel channel) {

        CreateGroupRequestPacket groupRequestPacket = new CreateGroupRequestPacket();

        System.out.println("拉人入群，请输入该用户的ID：(用户ID以,分割)");
        String userIdStr = scanner.next();
        //
        Set<String> userIds = Sets.newHashSet(StringUtils.split(userIdStr, USER_ID_SPLITER));
        //添加自己
        MySession session = MySessionUtil.getSession(channel);
        userIds.add(session.getUserId());

        groupRequestPacket.setUserIdList(Lists.newArrayList(userIds));
        channel.writeAndFlush(groupRequestPacket);

    }
}
