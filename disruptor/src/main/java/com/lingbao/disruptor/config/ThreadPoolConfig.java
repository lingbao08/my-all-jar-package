package com.lingbao.disruptor.config;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import static com.lingbao.disruptor.constants.Constants.CPU_CORE;

/**
 * @author lingbao08
 * @DESCRIPTION
 * @create 2020-08-24 22:53
 **/
@Component
public class ThreadPoolConfig {

    private static final AtomicLong orderNum = new AtomicLong(0);
    //最大线程数
    // 1. 如果是计算密集型，有大量的计算，像spark的流式计算，公式是：CPU核数+1  或者 CPU核数*2
    // 2. IO密集型 ，有大量的输入输出，比如用线程池异步存储到DB，写入到文件系统中。 公式是：CPU核数/(1-(0.8~0.9))
    //  以上是针对于一个JVM来说的，所以在建立线程池时，需要考虑诸多因素，要考虑该服务器上所有的线程池的使用数量。


    //队列长度的确定：
    // 首先，队列要使用有界队列，即 ArrayBlockingQueue。
    // 队列的长度，需要考虑到

    @Bean
    public ThreadPoolExecutor orderThreadPoolExecutor() {
        return new ThreadPoolExecutor(CPU_CORE, CPU_CORE << 1,
                10, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(200),
                r -> new Thread(r) {{
                    setName("order-exec-" + orderNum.incrementAndGet());
                }},
                (r, executor1) -> System.out.println("我要拒绝啦"));
    }
    //线程池的关闭
    // 关闭服务使用`kill -15`来关闭
    // 使用hook来关闭线程池（？？？暂时不知道怎么关闭）
}
