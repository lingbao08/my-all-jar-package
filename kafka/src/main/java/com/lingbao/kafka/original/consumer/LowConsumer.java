package com.lingbao.kafka.original.consumer;

import com.google.common.collect.Lists;
import com.lingbao.base.utils.DateUtil;
import kafka.api.FetchRequest;
import kafka.api.FetchRequestBuilder;
import kafka.cluster.BrokerEndPoint;
import kafka.javaapi.*;
import kafka.javaapi.consumer.SimpleConsumer;
import kafka.javaapi.message.ByteBufferMessageSet;
import kafka.message.MessageAndOffset;
import lombok.extern.slf4j.Slf4j;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

/**
 * 使用场景：在已知你的某个月的数据存在某个分区，
 * 这个时候你就需要直接对该分区（的指定offset）进行拉取，来获取你想要的数据。
 *
 * @author lingbao08
 * @DESCRIPTION
 * @create 2020-09-26 23:40
 **/
@Slf4j
public class LowConsumer {

    public static final String TOPIC = "topic";
    public static final int PARTITION = 1;

    public static void main(String[] args) throws Exception {

        //这个host和port是随机的一个kafka的地址和端口
        String host = "localhost";
        int port = 9092;
        //目标topic的目标分区的地址信息
        BrokerEndPoint leader = null;
        //============== 1. 获取元数据信息 =================================================================
        SimpleConsumer metaConsumer = new SimpleConsumer(host, port,
                500, 10 * 1024, "metadata");
        TopicMetadataResponse topicResp = metaConsumer.send(
                new TopicMetadataRequest(Lists.newArrayList(TOPIC)));

        // 代码段，在下面用于直接加快跳出2层for循环，避免多余的循环
        // 遍历所有的topic和partition，找到你需要找的topic的指定partition的leader节点的元数据。
        leaderLabel:
        for (TopicMetadata metadata : topicResp.topicsMetadata()) {
            if (TOPIC.equals(metadata.topic())) {
                for (PartitionMetadata partitionsMetadata : metadata.partitionsMetadata()) {
                    if (PARTITION == partitionsMetadata.partitionId()) {
                        leader = partitionsMetadata.leader();
                        break leaderLabel;
                    }
                }
            }
        }

        if (leader == null) {
            log.error("没有找到目标主题目标分区的leader节点");
            return;
        }
        //============= 2. 获取消息数据信息 ================================================================
        // 拉取超时时间是500ms,或者说到达bufferSize（1024*10）满了，就会返回。
        // clientId是当前拉取数据的客户端ID，唯一即可
        SimpleConsumer dataConsumer = new SimpleConsumer(leader.host(), leader.port(),
                500, 10 * 1024, "data");
        //采用build来构建
        FetchRequest req = new FetchRequestBuilder()
                //如果不知道offset，那就从0开始，如果知道offset，那么就从指定的offset开始消费
                //每次拉取1024*10个长度的数据
                .addFetch(TOPIC, PARTITION, 0, 10 * 1024).build();
        FetchResponse fetchResp = dataConsumer.fetch(req);
        ByteBufferMessageSet messageSet = fetchResp.messageSet(TOPIC, PARTITION);

        for (MessageAndOffset messageAndOffset : messageSet) {
            long timestamp = messageAndOffset.message().timestamp();
            log.info("这个消息是在{}时间发送的", DateUtil.toString(timestamp));
            //获取消息体
            ByteBuffer payload = messageAndOffset.message().payload();
            byte[] bytes = new byte[payload.limit()];
            payload.get(bytes);
            log.info("找到的内容为：{}", new String(bytes, StandardCharsets.UTF_8));
        }


    }
}
