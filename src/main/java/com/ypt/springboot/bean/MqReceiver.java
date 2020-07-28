package com.ypt.springboot.bean;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class MqReceiver {
    public static void main(String[] args) {
        ConnectionFactory connectionFactory;
        Connection connection = null;
        Session session;
        Destination destination;
        MessageConsumer consumer;
        connectionFactory = new ActiveMQConnectionFactory(
                ActiveMQConnectionFactory.DEFAULT_USER,
                ActiveMQConnectionFactory.DEFAULT_PASSWORD,
                ActiveMQConnectionFactory.DEFAULT_BROKER_URL
        );
        try {
            // 构造从工厂得到连接对象
            connection = connectionFactory.createConnection();
            // 启动
            connection.start();
            // 获取操作连接
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            // 获取session注意参数值是一个服务器的queue/topic
            // destination = session.createQueue("FirstQueue");
            destination = session.createQueue("queue");
            consumer = session.createConsumer(destination);
            while (true) {
                //设置接收者接收消息的超时时间
                TextMessage message = (TextMessage) consumer.receive(100000);
                if (null != message) {
                    System.out.println("收到消息" + message.getText());
                    message.acknowledge();
                } else {
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != connection)
                    connection.close();
            } catch (Throwable ignore) {
            }
        }
    }

}
