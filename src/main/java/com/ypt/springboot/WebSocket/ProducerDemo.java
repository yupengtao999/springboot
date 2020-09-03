//package com.ypt.springboot.WebSocket;
//
//import org.apache.kafka.clients.producer.*;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Properties;
//
//public class ProducerDemo {
//    private static final String helloTopic = "HelloWorld";
//
//    public static void main(String[] args) {
//        // 1. 构造Propertity，进行producer 相关配置。
//        Properties properties = new Properties();
//        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "172.16.6.151:9092");
//        properties.put(ProducerConfig.ACKS_CONFIG, "all");
//        properties.put(ProducerConfig.RETRIES_CONFIG, 0);
//        properties.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
//        properties.put(ProducerConfig.LINGER_MS_CONFIG, 1);
//        // 消息序列化方式
//        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
//        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
//        Producer<String, String> producer = null;
//        try {
//            // 2. 构造Producer对象
//            producer = new KafkaProducer<>(properties);
//            for (int i = 0; i < 10; i++) {
//                String msgValue = "Message " + i;
//                // 3. 发送消息
//                producer.send(new ProducerRecord<>(helloTopic, msgValue), new Callback() {
//                    @Override
//                    public void onCompletion(RecordMetadata recordMetadata, Exception e) {
//                        List a = new ArrayList();
//                    }
//                });
//                System.out.println("Sent:" + msgValue);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (producer != null) {
//                producer.close();
//            }
//        }
//    }
//
//}
