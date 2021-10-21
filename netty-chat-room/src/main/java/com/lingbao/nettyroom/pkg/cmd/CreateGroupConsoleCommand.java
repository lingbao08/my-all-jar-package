package com.lingbao.nettyroom.pkg.cmd;

import com.google.common.collect.Lists;
import com.lingbao.nettyroom.packet.request.CreateGroupRequestPacket;
import com.lingbao.nettyroom.pkg.login.MySession;
import com.lingbao.nettyroom.pkg.login.MySessionUtil;
import io.netty.channel.Channel;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.Arrays;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

import static com.lingbao.nettyroom.constant.Constants.USER_ID_SPLINTER;

/**
 * @author lingbao08
 * @DESCRIPTION
 * @create 2019-02-24 16:34
 **/

public class CreateGroupConsoleCommand implements ConsoleCommand {


    @Override
    public void exec(Scanner scanner, Channel channel) {

        CreateGroupRequestPacket groupRequestPacket = new CreateGroupRequestPacket();

        System.out.println("拉人入群，请输入该用户的ID：(用户ID以,分割)");
        String userIdStr = scanner.next();
        //将字符串用户ID转换成数字用户ID，然后过滤掉不正确的用户ID
        String[] split = StringUtils.split(userIdStr, USER_ID_SPLINTER);

        Set<Integer> userIds =
                Arrays.stream(split).parallel()
                        .map(NumberUtils::toInt).filter(x -> x > 0).collect(Collectors.toSet());
        //添加自己
        MySession session = MySessionUtil.getSession(channel);
        userIds.add(session.getMember().getUserId());

        groupRequestPacket.setUserIdList(Lists.newArrayList(userIds));
        channel.writeAndFlush(groupRequestPacket);

    }
}
