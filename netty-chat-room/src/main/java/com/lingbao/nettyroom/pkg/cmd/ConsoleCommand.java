package com.lingbao.nettyroom.pkg.cmd;

import io.netty.channel.Channel;

import java.util.Scanner;

/**
 * 所有请求都会经过命令行{@link ConsoleCommand}的子类，然后在子类中会新建一个packet，然后传递下去。
 * 所有的{@link com.lingbao.nettyroom.pipeline.server}目录下的Handler会看是否对应这个packet然后进行处理。
 *
 * @author lingbao08
 * @DESCRIPTION
 * @create 2019-02-24 16:30
 **/
@FunctionalInterface
public interface ConsoleCommand {

    void exec(Scanner scanner, Channel channel);
}
