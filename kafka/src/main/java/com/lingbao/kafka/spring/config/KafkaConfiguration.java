package com.lingbao.kafka.spring.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;

import java.util.HashMap;
import java.util.Map;

import static org.apache.kafka.clients.consumer.ConsumerConfig.*;

/**
 * @author lingbao08
 * @desc 更全的配置
 * @date 5/17/21 19:51
 **/

@Configuration
@EnableKafka
public class KafkaConfiguration {

    @Value("${spring.kafka.bootstrap-servers}")
    private String servers;// kafka server地址
    @Value("${spring.kafka.consumer.enable-auto-commit}")
    private boolean enableAutoCommit; // 是否自动提交
    @Value("${spring.kafka.consumer.session-timeout-ms}")
    private String sessionTimeout; // session失效时间
    @Value("${spring.kafka.consumer.auto-commit-interval}")
    private String autoCommitInterval; // 自动提交的间隔时间 ms
    @Value("${spring.kafka.consumer.group-id}")
    private String groupId; // topic组ID
    @Value("${spring.kafka.consumer.auto-offset-reset}")
    private String autoOffsetReset; // 最早未被消费的offset
    @Value("${spring.kafka.consumer.concurrency}")
    private int concurrency;// 消费线程并发数量

    @Value("${spring.kafka.consumer.max-poll-records:1000}")
    private String maxPollRecords;// 每次拉去最大数量

    @Value("${spring.kafka.consumer.max-poll-interval-ms:500000}")
    private String maxPollInterval; // 最大poll数据时间间隔

    @Bean("kafkaListenerContainerFactory")
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>>
    kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        factory.setConcurrency(concurrency);
        factory.getContainerProperties().setPollTimeout(5000);
//        factory.setBatchListener(true);
        return factory;
    }

    public ConsumerFactory<String, String> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfigs());
    }

    public Map<String, Object> consumerConfigs() {
        Map<String, Object> propsMap = new HashMap<>();
        propsMap.put(BOOTSTRAP_SERVERS_CONFIG, servers);
        propsMap.put(ENABLE_AUTO_COMMIT_CONFIG, enableAutoCommit);
        propsMap.put(AUTO_COMMIT_INTERVAL_MS_CONFIG, autoCommitInterval);
        propsMap.put(SESSION_TIMEOUT_MS_CONFIG, sessionTimeout);
        propsMap.put(KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        propsMap.put(VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        propsMap.put(GROUP_ID_CONFIG, groupId);
        propsMap.put(MAX_POLL_INTERVAL_MS_CONFIG, maxPollInterval);
        propsMap.put(AUTO_OFFSET_RESET_CONFIG, autoOffsetReset);
        propsMap.put(MAX_POLL_RECORDS_CONFIG, maxPollRecords);
        return propsMap;
    }

    /**
     * 自定义工厂，可以移除部分配置
     * 此处移除的是分组ID
     * 在注解 org.springframework.kafka.annotation.KafkaListener 中的 containerFactory属性上使用
     *
     * @return
     */
    @Bean
    public KafkaListenerContainerFactory<?> kafkaManualContainerFactory() {
        Map<String, Object> propsMap = consumerConfigs();
        propsMap.remove(GROUP_ID_CONFIG);

        ConcurrentKafkaListenerContainerFactory<String, String> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        ConsumerFactory<String, String> consumerFactory =
                new DefaultKafkaConsumerFactory<>(propsMap);
        factory.setConsumerFactory(consumerFactory);
        factory.setConcurrency(concurrency);
        factory.getContainerProperties().setPollTimeout(5000);
        return factory;
    }
}

