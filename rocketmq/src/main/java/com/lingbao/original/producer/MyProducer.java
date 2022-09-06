package com.lingbao.original.producer;

import com.lingbao.common.RMQConsts;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.UUID;

/**
 * @author lingbao08
 * @desc 使用这个命令来启动rocket的docker镜像
 * `docker run -d --name rocketmq-dashboard -e "JAVA_OPTS=-Drocketmq.namesrv.addr=192.168.0.101:9876" -p 8089:8080 \
 * -t apacherocketmq/rocketmq-dashboard:latest`
 * <p>
 * 然后打开页面 http://localhost:8089/#/ 就可以看到了
 * @date 2022/9/4 11:31
 **/
@Slf4j
public class MyProducer {

    public static void main(String[] args) throws Exception {

        // 无论是否使用事务机制，producer的groupId都一定要有。
        // 一个应用，只允许有一个 producerGroup。多个会报错
        DefaultMQProducer producer = new DefaultMQProducer("xiao_san");

        producer.setNamesrvAddr(RMQConsts.NAME_SVR);

        System.setProperty("rocketmq.client.logRoot", "/usr/local/rocketmq-4.9.3/logs/");

        producer.start();
        for (int i = 0; i < 4; i++) {
            // 如果没有topic，会抛出`No route info of this topic`错误。默认不创建topic，即使使用了createTopic。
            // 可以通过这个key在rocketmq的控制台进行消息查询（响应的MessageId也可以进行查询）
            Message message = new Message(RMQConsts.TEST_TOPIC, "tag-A", "key" + Instant.now().toString() + i,
                    "hello world".getBytes(StandardCharsets.UTF_8));

            // 1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h
            // 没有枚举类。设置为1，表示延迟1s。设置为4表示延迟30s。当设置大于18时，按照18来计算，即2h。
            if (i == 2)
                message.setDelayTimeLevel(2);

            // 最简单的方式
            SendResult sendResult = producer.send(message);

            log.info("消息已经发出：key:{},result:{}", message.getKeys(), sendResult.getSendStatus());
        }


        // 本地使用，设定5秒后退出
        Thread.sleep(5000);
        //注意退出
        System.exit(0);
    }

}
