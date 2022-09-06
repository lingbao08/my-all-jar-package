package com.lingbao.kafka.original.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.ProducerConfig;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

/**
 * @author lingbao08
 * @DESCRIPTION
 * @create 2020-09-26 22:29
 **/
@Slf4j
public class MyConsumer {

    public static void main(String[] args) throws Exception {

        Properties prop = new Properties();

        prop.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringDeserializer");
        prop.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringDeserializer");
        prop.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        prop.setProperty(ConsumerConfig.GROUP_ID_CONFIG, "wx001");


        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(prop);
        Map<Object, Object> objectObjectMap = Collections.synchronizedMap(new HashMap<>());
        consumer.subscribe(Arrays.asList("topic"));

        int i = 1;
        while (true) {
            // 读取数据，超时为100ms
            // 如果poll没有读到数据，下面的代码是不会执行的。只有在拉取到数据后，才会执行。
            // 读到数据，默认最大拉取500条。
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
            i++;
            if (records.isEmpty()) {
                log.info("没有数据，睡5秒");
                LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(5L * i));
                continue;
            }
            for (ConsumerRecord<String, String> record : records) {
                log.info("消费的偏移量：{},消费的分区：{},消费的key:{},消息的值:{}",
                        record.offset(), record.partition(), record.key(), record.value());
            }
            //提交
            consumer.commitSync();
        }

    }

}
