package com.lingbao.original.producer;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.lingbao.common.RMQConsts;
import com.lingbao.original.listener.MyTransListener;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.mutable.Mutable;
import org.apache.rocketmq.client.producer.*;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.*;

/**
 * @author lingbao08
 * @desc
 * @date 2022/9/5 16:38
 **/
@Slf4j
public class MyTxProducer {

    public static void main(String[] args) throws Exception {
        // 无论是否使用事务机制，producer的groupId都一定要有。
        // 一个应用，只允许有一个 producerGroup。多个会报错
        TransactionMQProducer producer = new TransactionMQProducer("xiao_san_tx_group");

        producer.setNamesrvAddr(RMQConsts.NAME_SVR);
        // 设置RMQ的client的日志
        System.setProperty("rocketmq.client.logRoot", "/usr/local/rocketmq-4.9.3/logs/");

        ThreadPoolExecutor executor =
                new ThreadPoolExecutor(4, 4, 1, TimeUnit.MINUTES,
                        new ArrayBlockingQueue<>(1000), r -> {
                    Thread thread = new Thread(r);
                    thread.setName("xiao_san_tx_group-");
                    return thread;
                });
        // 设置事务执行线程池
        producer.setExecutorService(executor);
        // 设置事务监听器：1）执行本地事务；2）回调检查。
        producer.setTransactionListener(new MyTransListener());

        producer.start();

        // 创建拦截闩，等待事务执行完毕
        CountDownLatch latch = new CountDownLatch(1);
        //消息体内容
        Map<String, Object> map = Map.of("userId", 1234, "bound", 9.15, "goods", "shirt");

        // 可以通过这个key在rocketmq的控制台进行消息查询（响应的MessageId也可以进行查询）
        Message message = new Message(RMQConsts.TEST_TOPIC, "tag-A", "key" + Instant.now().toString(),
                JSON.toJSONString(map).getBytes(RemotingHelper.DEFAULT_CHARSET));

        // 这个地方的arg 对应到 MyTransListener 中的 executeLocalTransaction方法的第二个参数
        // 第二个参数是一个obj，你可以传任何你需要的类型
        Map<String, Object> argMap = Maps.newHashMap(map);
        argMap.put("sale_activity", true);
        argMap.put("latch", latch);
        TransactionSendResult sendResult = producer.sendMessageInTransaction(message, argMap);
        // 等待5秒，如果本地事务执行完毕。就执行下面的逻辑
        latch.await(5, TimeUnit.SECONDS);

        if (sendResult.getSendStatus() == SendStatus.SEND_OK &&
                sendResult.getLocalTransactionState() == LocalTransactionState.COMMIT_MESSAGE) {
            log.info("消息发送成功：key:{}", message.getKeys());
        } else {
            log.info("消息发送错误：key:{},send status:{},tx status:{}", message.getKeys(),
                    sendResult.getSendStatus(), sendResult.getLocalTransactionState());
        }

        // 本地使用，设定5秒后退出
        Thread.sleep(5000);
        //注意退出
        System.exit(0);


    }
}
