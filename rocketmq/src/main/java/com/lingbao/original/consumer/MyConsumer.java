package com.lingbao.original.consumer;

import com.alibaba.fastjson.JSON;
import com.lingbao.common.RMQConsts;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.remoting.common.RemotingHelper;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author lingbao08
 * @desc
 * @date 2022/9/4 13:36
 **/
@Slf4j
public class MyConsumer {

    public static void main(String[] args) throws Exception {

        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(RMQConsts.TEST_TOPIC);

        consumer.setNamesrvAddr(RMQConsts.NAME_SVR);

        // 设置从队列消费
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);
        // 设置最大重试次数，默认是16次
        consumer.setMaxReconsumeTimes(5);

        System.setProperty("rocketmq.client.logRoot", "/usr/local/rocketmq-4.9.3/logs/");

        // 订阅 TEST_TOPIC topic下的 tag-A 标签的所有消息 。此处要订阅该topic下所有消息，第二个参数设置为*。
        // NOT ！！ 不能使用tag-*的形式来进行正则匹配
        consumer.subscribe(RMQConsts.TEST_TOPIC, "tag-A");

        // 注册消息消费事件
        consumer.registerMessageListener((MessageListenerConcurrently) (list, consumeConcurrentlyContext) -> {
            try {
                log.info("MessageListenerConcurrently。。。");
                for (MessageExt messageExt : list) {
                    if (messageExt.getReconsumeTimes() > 4) {
                        // 如果4次消费错误，那么就需要入库，然后手动处理了。人工介入
                        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                    }
                    if (messageExt.getKeys().equals("key2022-09-05T08:05:07.559Z3")) {
                        // 使用rocket内部的计数器，可以防止服务器宕机导致的多余次数消费
                        throw new RuntimeException("XXXXXXX" + messageExt.getReconsumeTimes());
                    }
                    String msgBody = new String(messageExt.getBody(), RemotingHelper.DEFAULT_CHARSET);
                    log.info("本条消息内容为：{}", msgBody);
                }
            } catch (Exception e) {
                log.error("消费失败，", e);
                // 默认会执行16次重试
                // RECONSUME_LATER 稍后重新消费的意思
                return ConsumeConcurrentlyStatus.RECONSUME_LATER;
            }
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        });

        consumer.start();

        log.info("消费者启动了。。。。");
    }
}
