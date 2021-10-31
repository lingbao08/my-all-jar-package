package com.lingbao.kafka.original.interceptor;

import com.alibaba.fastjson.JSON;
import com.lingbao.kafka.model.MyRecord;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 一个简单的数据统计拦截器，统计发送成功次数，发送失败次数
 * <p>
 * kafka的拦截器，可以用来修改消息等
 * <p>
 * prop.put(ProducerConfig.INTERCEPTOR_CLASSES_CONFIG,
 * Arrays.asList("com.lingbao.kafka.original.interceptor.MyInterceptor"));
 * </p>
 * kafka的拦截器可以传入list，当前面的拦截器出现异常，并不影响后面拦截器的执行
 * <p>
 * 首先执行拦截器，然后再执行其他操作
 *
 * 拦截器的顺序，请查看生产者最上面的配置参数{@link ProducerConfig#INTERCEPTOR_CLASSES_CONFIG}
 *
 * @author lingbao08
 * @DESCRIPTION
 * @create 2020-09-27 00:29
 **/
@Slf4j
public class CountInterceptor implements ProducerInterceptor<String, String> {

    private static AtomicInteger successNum = new AtomicInteger();
    private static AtomicInteger failureNum = new AtomicInteger();

    /**
     * 在该部分整理完数据后才会进行发送
     *
     * @param record
     * @return
     */
    @Override
    public ProducerRecord<String, String> onSend(ProducerRecord<String, String> record) {
        //切记不要返回null，返回原始数据
        //如果不需要修改就返回原始的record就可以了。

        // return record;

        String oldVal = record.value();

        MyRecord myRecord = JSON.parseObject(oldVal, MyRecord.class);
        myRecord.setTimestamp(System.currentTimeMillis());

        String newVal = JSON.toJSONString(myRecord);

        //因为我们没有用到key，ProducerRecord最基本的入参就是topic和value了。
        //如果有用到别的值，那么这里需要用别的构造函数进行返回。。。ProducerRecord不能进行修改，只有重新new
        //工作中不确定的参数，需要都给补上

        // key 和 partition
        // 如果指定了有效的partition，那么就用partition；否则用key hash 求分区。若没有key，随机发送。

        return new ProducerRecord<>(record.topic(), newVal);
    }

    @Override
    public void onAcknowledgement(RecordMetadata metadata, Exception exception) {
        //统计成功和失败的数量
        if (exception == null) {
            successNum.getAndIncrement();
        } else {
            failureNum.getAndIncrement();
        }
    }

    @Override
    public void close() {
        log.info("数据发送成功{}次，数据发送失败{}次", successNum, failureNum);
    }


    @Override
    public void configure(Map<String, ?> configs) {

    }
}
