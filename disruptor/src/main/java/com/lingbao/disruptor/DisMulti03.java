package com.lingbao.disruptor;

import com.lingbao.disruptor.handler.mult.*;
import com.lingbao.disruptor.model.Order;
import com.lingbao.disruptor.threads.MyPublish;
import com.lmax.disruptor.ExceptionHandler;
import com.lmax.disruptor.SleepingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StopWatch;

import java.util.concurrent.*;

import static com.lingbao.disruptor.threads.MyPublish.PUBLISH_COUNT;

/**
 * @author lingbao08
 * @desc
 * @date 5/17/21 22:14
 **/

@Slf4j
public class DisMulti03 {

    public static final ScheduledExecutorService scheduled = Executors.newScheduledThreadPool(1);

    public static void main(String[] args) throws InterruptedException {

        StopWatch watch = new StopWatch();
        watch.start("TASK-3");

        scheduled.scheduleAtFixedRate(() -> {
//            JvmMetricsInfo.getCPU();
            log.info("---------------------------------"+System.currentTimeMillis());
        }, 0,1, TimeUnit.SECONDS);

        // NOTICE 这种方式的消费的线程数必须大于等于eventHandler的数量
        ExecutorService es1 = Executors.newFixedThreadPool(10);
        ExecutorService es2 = Executors.newFixedThreadPool(10);
        //1. 构建 disruptor
        Disruptor<Order> disruptor = new Disruptor<>(
                Order::new, 2 << 19, es1, ProducerType.MULTI, new SleepingWaitStrategy());
        //2. 把消费者设置到disruptor中 handlerEvent

        // 2.1 串行操作 总耗时 6s
//        disruptor
//                .handleEventsWith(new Handler1())
//                .handleEventsWith(new Handler2())
//                .handleEventsWith(new Handler3());

        // 2.2 全部并行操作 总耗时 3s
        // 等价于 handleEventsWith(new Handler1(),new Handler3(),new Handler2())
//        disruptor.handleEventsWith(new Handler1());
//        disruptor.handleEventsWith(new Handler3(),new Handler2());

        //2.3 部分并行操作 总耗时4s
        // 2,3是并行，1和23组成的部分是串行。
//        disruptor
//                .handleEventsWith(new Handler1())
//                .handleEventsWith(new Handler3(),new Handler2());

        // 2.4
        //等价于2.3
//        disruptor
//                .handleEventsWith(new Handler1())
//                .then(new Handler3(),new Handler2());

        // 2.5 菱形操作
        // h1->h2->h3<-
        // |->h4->h5--|
        Handler1 h1 = new Handler1();
        Handler2 h2 = new Handler2();
        Handler3 h3 = new Handler3();
        Handler4 h4 = new Handler4();
        Handler5 h5 = new Handler5();

        //      h2
        //    /   \
        // h1 -h4-- h3
        //    \   /
        //     h5
        //h2,h4,h5只会经过一个
        disruptor.handleEventsWithWorkerPool(h1)
                .thenHandleEventsWithWorkerPool(h2, h4, h5)
                .thenHandleEventsWithWorkerPool(h3);

        //3. 启动 disruptor
        disruptor.start();


        CountDownLatch latch = new CountDownLatch(PUBLISH_COUNT);

        es2.submit(new MyPublish<>(latch, disruptor));


        latch.await();
        log.info("await结束了：{}",latch.getCount());
        disruptor.shutdown();
        es1.shutdown();
        es2.shutdown();
        watch.stop();
        scheduled.shutdown();
        log.info("总耗时：{}",watch.getTotalTimeSeconds());
        log.info(watch.prettyPrint());
    }

    @Slf4j
    static class MyEventExHandler implements ExceptionHandler<Order> {
        @Override
        public void handleEventException(Throwable ex, long sequence, Order event) {
            log.info("handleEventException");
        }

        @Override
        public void handleOnStartException(Throwable ex) {
            log.info("handleOnStartException");
        }

        @Override
        public void handleOnShutdownException(Throwable ex) {
            log.info("handleOnShutdownException");
        }
    }
}
