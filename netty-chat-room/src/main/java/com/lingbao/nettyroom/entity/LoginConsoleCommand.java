package com.lingbao.nettyroom.entity;

import com.lingbao.nettyroom.pkg.cmd.ConsoleCommand;
import io.netty.channel.Channel;

import java.util.Scanner;

/**
 * @author lingbao08
 * @DESCRIPTION
 * @create 2019-02-24 16:46
 **/

public class LoginConsoleCommand implements ConsoleCommand {

    @Override
    public void exec(Scanner scanner, Channel channel) {
        LoginRequestPacket lrp = new LoginRequestPacket();
        System.out.println("输入你的用户名: ");
        String line = scanner.nextLine();
        lrp.setUserId(80);
        lrp.setUsername(line);
        lrp.setPassword("pwd");
        channel.writeAndFlush(lrp);
        waitSleep();
    }

    public static void waitSleep() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
