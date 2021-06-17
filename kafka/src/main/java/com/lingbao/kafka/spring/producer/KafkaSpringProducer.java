package com.lingbao.kafka.spring.producer;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * @author lingbao08
 * @desc
 * @date 5/17/21 20:05
 **/

@Slf4j
@Component
public class KafkaSpringProducer {

    @Autowired
    private KafkaTemplate kafkaTemplate;


    public boolean send(Object o) {
        try {
            kafkaTemplate.send("topic", JSON.toJSONString(o));
            return true;
        } catch (Exception e) {
            log.error("kafka发送消息错误", e);
            return false;
        }
    }

    public boolean sendWithPartition(Object o) {
        try {
            // 发送给下标为1的分区
            // key % 分区总数
            kafkaTemplate.send("topic", 1,JSON.toJSONString(o));
            return true;
        } catch (Exception e) {
            log.error("kafka发送消息错误", e);
            return false;
        }
    }
}
