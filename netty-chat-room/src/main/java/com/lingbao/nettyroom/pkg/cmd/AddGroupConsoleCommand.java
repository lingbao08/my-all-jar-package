package com.lingbao.nettyroom.pkg.cmd;

import com.lingbao.nettyroom.packet.request.AddGroupRequestPacket;
import io.netty.channel.Channel;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.Scanner;

/**
 * @author lingbao08
 * @DESCRIPTION
 * @create 2019-02-24 17:53
 **/

public class AddGroupConsoleCommand implements ConsoleCommand {
    @Override
    public void exec(Scanner scanner, Channel channel) {
        AddGroupRequestPacket addGroupRequestPacket = new AddGroupRequestPacket();
        System.out.println("请输入组ID：");

        String next = scanner.next();

        if(StringUtils.isEmpty(next)){
            System.out.println("组ID不能为空！");
            return;
        }

        if (!StringUtils.startsWith(next, "G")) {
            next = "G-" + NumberUtils.toInt(next);
        }


        addGroupRequestPacket.setGroupId(next);
        channel.writeAndFlush(addGroupRequestPacket);

    }
}
