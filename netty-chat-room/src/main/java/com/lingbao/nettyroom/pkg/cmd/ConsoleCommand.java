package com.lingbao.nettyroom.pkg.cmd;

import io.netty.channel.Channel;

import java.util.Scanner;

/**
 * @author lingbao08
 * @DESCRIPTION
 * @create 2019-02-24 16:30
 **/

public interface ConsoleCommand {

    void exec(Scanner scanner, Channel channel);
}
