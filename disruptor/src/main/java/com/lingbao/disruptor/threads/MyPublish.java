package com.lingbao.disruptor.threads;

import com.lingbao.disruptor.model.Order;
import com.lmax.disruptor.EventTranslator;
import com.lmax.disruptor.dsl.Disruptor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Random;
import java.util.concurrent.CountDownLatch;

/**
 * @author lingbao08
 * @desc
 * @date 5/17/21 22:13
 **/

@Slf4j
@Data
@AllArgsConstructor
public class MyPublish<T> implements Runnable {

    private CountDownLatch latch;

    private Disruptor<T> disruptor;

    public static final int PUBLISH_COUNT = 10000;

    @Override
    public void run() {
        for (int i = 0; i < PUBLISH_COUNT; i++) {
            //新的任务提交方式
            disruptor.publishEvent(new MyEventTranslator<>());
            latch.countDown();
        }
    }


    @Slf4j
   static class MyEventTranslator<T> implements EventTranslator<T> {

        private Random random = new Random();

        @Override
        public void translateTo(T event, long sequence) {
            generateOrder(event);
        }

        private void generateOrder(T event) {
            Order order = (Order) event;
            order.setOrderName("Order");
            order.setOrderId(random.nextInt(99));
            log.info(order.toString());
        }
    }

}
