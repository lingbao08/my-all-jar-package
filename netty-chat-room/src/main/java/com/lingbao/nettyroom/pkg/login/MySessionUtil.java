package com.lingbao.nettyroom.pkg.login;


import com.lingbao.nettyroom.constant.Attributes;
import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @author lingbao08
 * @DESCRIPTION
 * @create 2019-02-24 15:13
 **/

public class MySessionUtil {

    /**
     * 所有用户自己的channel<用户ID，channel>
     */
    private static final ConcurrentHashMap<Integer, Channel> channelMap = new ConcurrentHashMap<>();

    /**
     * 所有群的channelGroup<组ID，ChannelGroup>
     */
    private static final ConcurrentHashMap<String, ChannelGroup> channelGroupMap = new ConcurrentHashMap<>();

    /**
     * 将session和channel进行绑定
     *
     * @param session
     * @param channel
     */
    public static void bindSession(MySession session, Channel channel) {
        channelMap.put(session.getMember().getUserId(), channel);
        channel.attr(Attributes.SESSION).set(session);
    }


    /**
     * 解绑session和channel
     *
     * @param channel
     */
    public static void unBindSession(Channel channel) {
        if (hasLogin(channel)) {
            channelMap.remove(getSession(channel).getMember().getUserId());
            channel.attr(Attributes.SESSION).set(null);
        }
    }

    /**
     * 校验该用户的channel中是否有session，如果没有，则说明该用户未曾登陆
     *
     * @param channel
     * @return
     */
    public static boolean hasLogin(Channel channel) {
        return channel.hasAttr(Attributes.SESSION);
    }

    /**
     * 获取channel中的session
     *
     * @param channel
     * @return
     */
    public static MySession getSession(Channel channel) {
        return channel.attr(Attributes.SESSION).get();
    }


    /**
     * 通过用户ID，获取对应的channel
     * 后续会用于通知，或者将指定用户的消息发送给某人
     *
     * @param userId
     * @return
     */
    public static Channel getChannel(Integer userId) {
        return channelMap.get(userId);
    }

    /**
     * 根据组ID获取channelGroup
     *
     * @param groupId
     * @return
     */
    public static ChannelGroup getChannelGroup(String groupId) {
        return channelGroupMap.get(groupId);
    }

    /**
     * 添加组
     *
     * @param groupId
     * @param channels
     */
    public static void addChannelGroup(String groupId, ChannelGroup channels) {
        channelGroupMap.put(groupId, channels);
    }

    /**
     * @param groupId
     * @param channelList
     */
    public static void removeChannelGroup(String groupId, List<Channel> channelList) {

        //TODO 可能是不能执行的
        ChannelGroup channels = channelGroupMap.get(groupId);
        if (channels != null) {
            channels.removeAll(channelList);
        }
        channelGroupMap.remove(groupId);
    }
}
