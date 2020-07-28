package com.ypt.springboot.WebSocket;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;
import java.util.Queue;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

public class Consumer {
    private static final String helloTopic = "HelloWorld";

    public static void main(String[] args) {
        // 1. 构造Propertity，进行consumer相关配置。
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "172.16.6.151:9092");
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "group_2");
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
        properties.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        properties.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "30000");
        // 消息反序列化方式
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");

        // 2. 生成消费实例
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);
        // 3. 订阅相应的 topic
        //    说明：可以消费多个topic: Arrays.asList(topic);
        //          topic支持正则表达式：如：subscribe(Pattern.compile("test.*")
        consumer.subscribe(Arrays.asList(helloTopic));
        BlockingDeque queue = new LinkedBlockingDeque();

        // 4. 循环消费消息
        while (true) {
            try {
                // 4.1 poll方法拉取订阅的消息, 消费者必须不断的执行poll，获取消息、维持连接。
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(100));
                // 4.2 消费数据，必须在下次poll之前消费完这些数据, 且总耗时不得超过SESSION_TIMEOUT_MS_CONFIG
                //     若不能在下次poll之前消费完，则会触发一次负载均衡，产生卡顿。
                //     可以开一个单独的线程池来消费消息，然后异步返回结果。
                for (ConsumerRecord<String, String> record : records) {
                    System.out.println("===========================");
                    System.out.printf("topic = %s, partition = %d, offset = %d, key = %s, value = %s\n",
                            record.topic(), record.partition(), record.offset(), record.key(), record.value());
                }
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                // 不再消费主动关闭
                consumer.close();
            }
        }
    }

}
