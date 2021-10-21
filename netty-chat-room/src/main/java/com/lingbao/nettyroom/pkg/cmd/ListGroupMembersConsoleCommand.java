package com.lingbao.nettyroom.pkg.cmd;

import com.lingbao.nettyroom.packet.request.ListGroupMembersRequestPacket;
import io.netty.channel.Channel;

import java.util.Scanner;

/**
 * @author lingbao08
 * @DESCRIPTION
 * @create 2019-02-24 17:54
 **/

public class ListGroupMembersConsoleCommand implements ConsoleCommand {
    @Override
    public void exec(Scanner scanner, Channel channel) {
        ListGroupMembersRequestPacket groupRequestPacket = new ListGroupMembersRequestPacket();

        channel.writeAndFlush(groupRequestPacket);
    }
}
