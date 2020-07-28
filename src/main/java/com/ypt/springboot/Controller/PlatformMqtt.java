package com.ypt.springboot.Controller;

import com.ypt.springboot.Config.ReportMqtt;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.Map;

@RestController
@Component
@RequestMapping("/mqtt")
public class PlatformMqtt implements MqttCallback {
    @Value("${mqtt.hostUrl}")
    public   String HOST ;
    @Value("${mqtt.username}")
    private  String name;
    @Value("${mqtt.password}")
    private  String passWord ;
    private MqttClient client;
    private MqttMessage message;
    private static final Logger LOGGER = LoggerFactory.getLogger(PlatformMqtt.class);
    //clientId不能重复所以这里我设置为系统时间
    String  clientid= "client";
    @PostConstruct
    public void send() {
        try {
            client = new MqttClient(HOST, clientid, new MemoryPersistence());
        } catch (MqttException e) {
            e.printStackTrace();
        }
        MqttConnectOptions options = new MqttConnectOptions();
        options.setCleanSession(false);
        options.setUserName(name);
        options.setPassword(passWord.toCharArray());
        // 设置超时时间
        options.setConnectionTimeout(10);
        // 设置会话心跳时间
        options.setKeepAliveInterval(5);
        try {
            client.setCallback(this);
            client.connect(options);
        } catch (Exception e) {
            LOGGER.info("platform-Mqtt客户端连接异常，异常信息："+e);
        }
    }

    @RequestMapping("/request")
    public void publish(@RequestParam String body,@RequestParam String topic) throws MqttException {
        topic="DataTopic/"+topic;
        LOGGER.info("请求主题为："+topic+",消息为："+body);
        message = new MqttMessage();
        message.setQos(0);
        message.setRetained(false);
        message.setPayload(body.getBytes());
        client.publish(topic,message);
    }

    @Override
    public void connectionLost(Throwable throwable) {
        LOGGER.info("程序出现异常，正在重新连接...:");
        try {
            client.close();
            send();
        } catch (MqttException e) {
            LOGGER.info(e.getMessage());
        }
        LOGGER.info("platform-Mqtt重新连接成功");
    }

    @Override
    public void messageArrived(String s, MqttMessage mqttMessage) {

    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
        LOGGER.info("消息发送成功");
    }
}
