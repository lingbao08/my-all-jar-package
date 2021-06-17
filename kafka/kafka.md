#kafka

###kafka原生API demo

####消费者部分

`com.lingbao.kafka.original.consumer.MyConsumer` 最简单的consumer，每隔一定时间拉取一次。

`com.lingbao.kafka.original.consumer.AsyncManuallyConsumer` 这是一个异步（默认是异步）手动提交消费进度的消费者。

`com.lingbao.kafka.original.consumer.SyncManuallyConsumer`  这是一个同步手动提交消费进度的消费者。

`com.lingbao.kafka.original.consumer.LowConsumer` 当你需要指定消费某个时间段某个分区下的数据时，可以使用这个demo。

####分区部分

`com.lingbao.kafka.original.partitioner.MyPartitioner`自定义分区实现。即可以按照自己的分区规则，来讲数据发送到指定分区。


####生产者部分
`com.lingbao.kafka.original.producer.MyProducer` 最简单生产者。

####拦截器部分

`com.lingbao.kafka.original.interceptor.CountInterceptor`用于拦截kafka消息，进行统计，或者在发送前修改kafka消息。


###kafka和springboot的demo

####配置部分

`com.lingbao.kafka.spring.config.KafkaConfiguration`。

一般情况下可以不定义`KafkaListenerContainerFactory`这个工厂，如有特殊化需求，可以定义。

配置的所有字段都可以在`org.apache.kafka.clients.consumer.ConsumerConfig`中找到。

####消费和生产
生产类：`com.lingbao.kafka.spring.producer.KafkaSpringProducer`

消费类：`com.lingbao.kafka.spring.consumer.KafkaSpringConsumer`

