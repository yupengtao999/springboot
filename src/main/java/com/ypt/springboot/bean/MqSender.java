package com.ypt.springboot.bean;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class MqSender {
    private static final int SENDNUM = 10;
    public static void main(String[] args) {
        ConnectionFactory connectionFactory;
        Connection connection = null;
        Session session;
        Destination destination;
        MessageProducer producer;
        connectionFactory = new ActiveMQConnectionFactory(
                ActiveMQConnectionFactory.DEFAULT_USER,
                ActiveMQConnectionFactory.DEFAULT_PASSWORD,
                ActiveMQConnectionFactory.DEFAULT_BROKER_URL
        );
        try {
            //构造从工厂得到连接对象
            connection = connectionFactory.createConnection();
            //启动
            connection.start();
            //获取操作连接
            session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
            // 获取session注意参数值是一个服务器的queue/topic
//            destination = session.createQueue("FirstQueue");
            destination = session.createQueue("queue");
            // 得到消息生成者【发送者】
            producer = session.createProducer(destination);
            // 持久化的设置，此处学习，实际根据项目决定
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
            // 构造消息，此处写死，项目就是参数，或者方法获取
            sendMessage(session, producer);
            session.commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if(null != connection){
                    connection.close();
                }
            } catch (Throwable ignore) {
            }
        }

    }
    public static void sendMessage(Session session,MessageProducer producer) throws JMSException {
        for (int i = 1;i <= SENDNUM;i++){
            TextMessage textMessage = session.createTextMessage("ActiveMq"+i);
            System.out.println("发送成功");
            producer.send(textMessage);
        }
    }
}
