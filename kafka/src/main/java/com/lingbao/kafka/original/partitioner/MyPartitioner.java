package com.lingbao.kafka.original.partitioner;

import com.alibaba.fastjson.JSON;
import com.lingbao.kafka.model.MyRecord;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;
import org.springframework.util.Assert;

import java.util.Map;

/**
 * kafka默认是走{@link org.apache.kafka.clients.producer.internals.DefaultPartitioner}这个类的
 * 当你在生产端的prop中设置了 ProducerConfig.PARTITIONER_CLASS_CONFIG,
 * 其值为当前类时，会走这个类。
 * <p>
 * 拦截器在分区器之前
 *
 * @author lingbao08
 * @DESCRIPTION
 * @create 2020-09-26 22:47
 **/
@Slf4j
public class MyPartitioner implements Partitioner {
    @Override
    public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
        // 控制分区
        Assert.notNull(value, "发送消息体value不能为空");
        log.info("发送的消息体为：{}", JSON.toJSONString(value));

        // 比如常见的根据ID进行分区
        // 你也可以把id传给send方法key值中，默认的分区器会执行按照分区取模的方法去执行
        MyRecord myRecord = JSON.parseObject(value.toString(), MyRecord.class);
        Integer partCount = cluster.partitionCountForTopic(topic);
        log.info("topic:{}的分区总数为:{}", topic, partCount);
        return myRecord.getId() % partCount;
    }

    @Override
    public void close() {
        log.info("分区结束时调用，这个方法一般也是用不上的。");
    }

    /**
     * 此方法不动
     *
     * @param map
     */
    @Override
    public void configure(Map<String, ?> map) {
    }
}
