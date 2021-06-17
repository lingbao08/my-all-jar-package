package com.lingbao.nettyroom.pkg.cmd;

import com.lingbao.nettyroom.entity.AddGroupRequestPacket;
import io.netty.channel.Channel;

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
        //如果是空的，就把自己的ID赋值给它
//        if(StringUtils.isEmpty(next)){
//            System.out.println("组ID不能为空！");
//            return;
//        }


        addGroupRequestPacket.setGroupId(next);
        channel.writeAndFlush(addGroupRequestPacket);

    }
}
