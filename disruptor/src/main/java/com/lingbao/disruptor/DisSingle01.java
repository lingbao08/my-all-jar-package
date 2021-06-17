package com.lingbao.disruptor;

import com.lingbao.disruptor.handler.single.*;
import com.lingbao.disruptor.model.Order;
import com.lingbao.disruptor.threads.MyPublish;
import com.lmax.disruptor.BusySpinWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StopWatch;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static com.lingbao.disruptor.threads.MyPublish.PUBLISH_COUNT;

/**
 * @author lingbao08
 * @desc
 * @date 5/17/21 22:09
 **/

@Slf4j
public class DisSingle01 {

    private static final AtomicInteger num = new AtomicInteger();

    public static void main(String[] args) throws InterruptedException {

        StopWatch watch = new StopWatch();
        watch.start("TASK-1");

        //消费的线程池（当线程数小于event数量时，会出现事件没有执行完，就结束的情况）
        ExecutorService es1 = Executors.newFixedThreadPool(5);
        //发布事件的线程池
        ExecutorService es2 = Executors.newFixedThreadPool(10);
        //1. 构建 disruptor
        Disruptor<Order> disruptor = new Disruptor<>(
                Order::new, 2 << 19, es1, ProducerType.SINGLE, new BusySpinWaitStrategy());
//        Disruptor<Order> disruptor2 = new Disruptor<>(
//                Order::new, 2 << 19, r -> {
//            Thread t = new Thread(null,  r,
//                    "DISRUPTOR-" + num.getAndIncrement(), 0);
//            if (t.isDaemon())
//                t.setDaemon(false);
//            if (t.getPriority() != Thread.NORM_PRIORITY)
//                t.setPriority(Thread.NORM_PRIORITY);
//            return t;
//        }, ProducerType.SINGLE, new BusySpinWaitStrategy());
        //2. 把消费者设置到disruptor中 handlerEvent
        Handler1 h1 = new Handler1();
        Handler2 h2 = new Handler2();
        Handler3 h3 = new Handler3();
        Handler4 h4 = new Handler4();
        Handler5 h5 = new Handler5();
        // 2.1 串行操作 总耗时 6s
        // h1->h2->h3
//        disruptor
//                .handleEventsWith(h1).handleEventsWith(h2).handleEventsWith(h3);

        // 2.2 全部并行操作 总耗时 3s
        // 等价于 handleEventsWith(h1,h2,h3)
        // h1,h2,h3这三个乱序执行，且都会执行 h1->(h2,h3)
//        disruptor.handleEventsWith(h1);
//        disruptor.handleEventsWith(h2,h3);

        //2.3 部分并行操作
        // h1->(h2,h4,h5)->h3。其中，h2,h4,h5三个并序乱序，且必会全部执行
        disruptor.handleEventsWith(h1)
                .handleEventsWith(h2, h4, h5)
                .handleEventsWith(h3);

        // 2.4
        //等价于2.3
//        disruptor
//                .handleEventsWith(new Handler1())
//                .then(new Handler3(),new Handler2());

        // 2.5 菱形操作
        // h1-> h2 -> h3 <-
        // |-> h4 -> h5 __|
//        disruptor.handleEventsWith(h1);
//        disruptor.after(h1).handleEventsWith(h2);
//        disruptor.after(h1).handleEventsWith(h4).handleEventsWith(h5);
//        disruptor.after(h2,h5).handleEventsWith(h3);

        //3. 启动 disruptor
        /*RingBuffer<Order> ringBuffer =*/
        disruptor.start();

        CountDownLatch latch = new CountDownLatch(PUBLISH_COUNT);

        es2.submit(new MyPublish<>(latch, disruptor));

        latch.await();
        disruptor.shutdown();
        es1.shutdown();
        es2.shutdown();
        watch.stop();
        System.out.println(watch.getTotalTimeSeconds());
        log.info(watch.prettyPrint());
    }
}
