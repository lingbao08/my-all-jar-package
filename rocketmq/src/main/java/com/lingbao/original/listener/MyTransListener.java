package com.lingbao.original.listener;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

/**
 * @author lingbao08
 * @desc
 * @date 2022/9/5 16:39
 **/
@Slf4j
public class MyTransListener implements TransactionListener {


    @Override
    public LocalTransactionState executeLocalTransaction(Message message, Object o) {

        try {
            log.info("执行本地事务操作。。。。");
            Map map = (Map) o;
            Object activity = map.get("sale_activity");
            CountDownLatch latch = (CountDownLatch)map.get("latch");
            // TODO 执行本地事务操作
            latch.countDown();
            log.info("activity:{}", activity);
        } catch (Exception e) {
            log.error("本地事务执行失败，", e);
            return LocalTransactionState.ROLLBACK_MESSAGE;
        }
        // UNKNOW 就是RMQ的broker是不可达的意思
//        return LocalTransactionState.UNKNOW;
        return LocalTransactionState.COMMIT_MESSAGE;
    }

    @Override
    public LocalTransactionState checkLocalTransaction(MessageExt messageExt) {
        log.info("回调消息检查。。。{}", messageExt.getKeys());

        try {
            // TODO 去数据库查询这笔订单的状况
            // 如果没有查询到，或者不符合预期，ROLLBACK
            String keys = messageExt.getKeys();

        } catch (Exception e) {
            log.error("回调消息检查失败，", e);
            return LocalTransactionState.ROLLBACK_MESSAGE;
        }
        return LocalTransactionState.COMMIT_MESSAGE;
    }
}
