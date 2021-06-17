package com.lingbao.kafka.original.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 这是一个手动提交的consumer
 *
 * @author lingbao08
 * @DESCRIPTION
 * @create 2020-09-28 20:54
 **/
@Slf4j
public class SyncManuallyConsumer {

    public static final ExecutorService pools = Executors.newFixedThreadPool(4);

    public static void main(String[] args) throws Exception {
        Properties prop = new Properties();

        prop.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringDeserializer");
        prop.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringDeserializer");
        //这个属性就是kafka每次poll的数量，默认值是500。这个值可以根据情况加大或者减小
        prop.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 200);
        //设置自动提交为false
        prop.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(prop);

        consumer.subscribe(Arrays.asList("topic1", "topic2"));

        while (true) {
            //读取数据，超时为100ms
            // 如果poll没有读到数据，下面的代码是不会执行的。只有在拉取到数据后，才会执行。
            // 读到数据，默认最大拉取500条。

            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));

//            CountDownLatch latch = new CountDownLatch(records.count());

            for (ConsumerRecord<String, String> record : records) {

                log.info("消费的偏移量：{},消费的分区：{},消费的key:{},消息的值:{}",
                        record.offset(), record.partition(), record.key(), record.value());
            }
            //同步提交
            consumer.commitSync();
        }
    }

}

