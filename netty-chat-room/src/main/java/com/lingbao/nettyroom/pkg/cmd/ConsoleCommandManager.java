package com.lingbao.nettyroom.pkg.cmd;

import io.netty.channel.Channel;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * @author lingbao08
 * @DESCRIPTION
 * @create 2019-02-24 16:31
 **/

public class ConsoleCommandManager implements ConsoleCommand {

    private Map<String, ConsoleCommand> cmdMap;

    public ConsoleCommandManager() {
        this.cmdMap = new HashMap<>();
        cmdMap.put("sd", new SendToUserConsoleCommand());
        cmdMap.put("out", new LogoutConsoleCommand());
        cmdMap.put("cg", new CreateGroupConsoleCommand());
        cmdMap.put("ag", new AddGroupConsoleCommand());
        cmdMap.put("ls", new ListGroupMembersConsoleCommand());
    }

    @Override
    public void exec(Scanner scanner, Channel channel) {
        String next = scanner.next();
        ConsoleCommand consoleCommand = cmdMap.get(next);
        if (consoleCommand != null)
            consoleCommand.exec(scanner, channel);
        else {
            System.out.println("无法识别的指令！" + next + "请尝试使用以下指令：\n"
                    + " - sd : 发送消息\n"
                    + " - out : 退出登录\n"
                    + " - cg : 创建组\n"
                    + " - ag : 添加组\n"
                    + " - ls : 展示群内成员");
        }
    }


}
