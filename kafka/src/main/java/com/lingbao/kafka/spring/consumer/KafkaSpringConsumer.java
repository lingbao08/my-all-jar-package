package com.lingbao.kafka.spring.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lingbao08
 * @desc
 * @date 5/17/21 20:02
 **/

public class KafkaSpringConsumer {

    /**
     * containerFactory指明
     *
     * @param records 记录
     *                <p>
     *                <code>
     * @KafkaListeners({@KafkaListener(topics="topic1"), @KafkaListener(topics="topic2")})等价于
     * @KafkaListener(topics="topic1")
     * @KafkaListener(topics="topic2") </code>
     * </p>
     */
    @KafkaListener(topics = {"topic"}, containerFactory = "kafkaManualContainerFactory")
    public void consumer(List<ConsumerRecord<String, Object>> records) {

        List<String> list = records.parallelStream()
                .map(ConsumerRecord::value)
                // list 强转为指定的类型，此处转换为String类型
                .map(x -> (String) x)
                .collect(Collectors.toList());

        list.forEach(System.out::println);

    }
}
