package com.lingbao.kafka.original.producer;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

/**
 * 一个简单的生产者
 *
 * @author lingbao08
 * @DESCRIPTION
 * @create 2020-09-26 22:29
 **/
@Slf4j
public class MyProducer {

    public static void main(String[] args) throws Exception {

        Properties prop = new Properties();
        //设置分区类
        prop.setProperty(ProducerConfig.PARTITIONER_CLASS_CONFIG, "com.lingbao.kafka.original.partitioner.MyPartitioner");
        //在0.9版本以后，offset（消费偏移量）默认保存在kafka集群中的，会生成50个分区文件夹(0.9之前是存在ZK中的)
        //而除此之外的元数据信息还是在ZK中的。比如topic的信息，比如消费者的注册等
        prop.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");

        prop.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringSerializer");
        prop.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringSerializer");
        prop.put(ProducerConfig.INTERCEPTOR_CLASSES_CONFIG,
                Lists.newArrayList("com.lingbao.kafka.original.interceptor.CountInterceptor"));
        //本地缓存存储的最大条数，默认值是16384
        prop.put(ProducerConfig.BATCH_SIZE_CONFIG, 200);
        //本地缓存发出去的延迟时间，默认不延迟。设置延迟时间有助于减少和broker的IO请求交互数量
        prop.put(ProducerConfig.LINGER_MS_CONFIG, 100);

        KafkaProducer<String, String> producer = new KafkaProducer<>(prop);

        //不带消息回执的
        producer.send(new ProducerRecord<>("topic", "hello kafka")).get();
        //带消息回执的
        producer.send(new ProducerRecord<>("topic", "hello kafka"),
                (metadata, e) -> {
                    if (metadata != null) {
                        log.info("发送的分区：{}，偏移量：{}", metadata.partition(), metadata.offset());
                    }
                });

        producer.close();
    }

}
