package com.lingbao.kafka.original.partitioner;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;

import java.util.Map;

/**
 * kafka默认是走{@link org.apache.kafka.clients.producer.internals.DefaultPartitioner}这个类的
 * 当你在生产端的prop中设置了 ProducerConfig.PARTITIONER_CLASS_CONFIG,
 * 其值为当前类时，会走这个类。
 * @author lingbao08
 * @DESCRIPTION
 * @create 2020-09-26 22:47
 **/

public class MyPartitioner implements Partitioner {
    @Override
    public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
        //控制分区
        return 0;
    }

    @Override
    public void close() {

    }

    @Override
    public void configure(Map<String, ?> map) {

    }
}
