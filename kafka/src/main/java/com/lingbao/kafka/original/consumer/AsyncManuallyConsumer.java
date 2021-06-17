package com.lingbao.kafka.original.consumer;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 这是一个手动提交的consumer
 *
 * @author lingbao08
 * @DESCRIPTION
 * @create 2020-09-28 20:54
 **/
@Slf4j
public class AsyncManuallyConsumer {

    public static final ExecutorService pools = Executors.newFixedThreadPool(4);

    public static void main(String[] args) throws Exception {
        Properties prop = new Properties();

        prop.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        prop.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        //这个属性就是kafka每次poll的数量，默认值是500。这个值可以根据情况加大或者减小
        prop.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 600);
        //设置自动提交为false
        prop.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(prop);

        consumer.subscribe(Arrays.asList("topic1", "topic2"));

        while (true) {
            //读取数据，超时为100ms
            // 如果poll没有读到数据，下面的代码是不会执行的。只有在拉取到数据后，才会执行。
            // 读到数据，默认最大拉取500条。

            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));

            CountDownLatch latch = new CountDownLatch(records.count());

            for (ConsumerRecord<String, String> record : records) {
                pools.submit(new ConCurrConsumerJob(record, latch));
            }

            // 最多等待200ms，然后进行提交
            latch.await(200, TimeUnit.MILLISECONDS);

            consumer.commitAsync();
        }

    }


    @Slf4j
    @AllArgsConstructor
    static class ConCurrConsumerJob implements Runnable {

        private ConsumerRecord<String, String> record;

        private CountDownLatch latch;

        //假如消费成功了，那么offset要变更为新的offset
        @Override
        public void run() {
            try {
                log.info("消费的偏移量：{},消费的分区：{},消费的key:{},消息的值:{}",
                        record.offset(), record.partition(), record.key(), record.value());
            } catch (Exception e) {
                log.error("消费失败了：{}", record);
                //可以将失败的记录写入到redis，便于事后进行观察
                //同时也可以统计数量以及数据特征等
            } finally {
                latch.countDown();
            }
        }
    }


}