package com.ypt.springboot.Config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class ReportMqtt implements MqttCallback {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReportMqtt.class);
    @Value("${mqtt.hostUrl}")
    public   String HOST ;
    @Value("${mqtt.topic}")
    public  String TOPIC;
    @Value("${mqtt.username}")
    private  String name;
    @Value("${mqtt.password}")
    private  String passWord ;
    private MqttClient client;
    private MqttConnectOptions options;     //clientId不能重复所以这里我设置为系统时间
    String  clientid= "mqtt";
    @PostConstruct
    public void result() {
        try {            // host为主机名，clientid即连接MQTT的客户端ID，一般以唯一标识符表示，MemoryPersistence设置clientid的保存形式，默认为以内存保存
            client = new MqttClient(HOST, clientid, new MemoryPersistence());
            // MQTT的连接设置
            options = new MqttConnectOptions();
            // 设置是否清空session,这里如果设置为false表示服务器会保留客户端的连接记录，这里设置为true表示每次连接到服务器都以新的身份连接
            options.setCleanSession(true);
            // 设置连接的用户名
            options.setUserName(name);
            // 设置连接的密码
            options.setPassword(passWord.toCharArray());
            // 设置超时时间 单位为秒
            options.setConnectionTimeout(10);
            // 设置会话心跳时间 单位为秒 服务器会每隔1.5*20秒的时间向客户端发送个消息判断客户端是否在线，但这个方法并没有重连的机制
            options.setKeepAliveInterval(5);
            // 设置回调
            client.setCallback(this);
            client.connect(options);
            //订阅消息
            int[] Qos = {0,0};
            String[] topic1 = {"DataTopic/"+TOPIC,"$SYS/brokers/+/clients/#"};
            client.subscribe(topic1, Qos);
        } catch (Exception e) {
            LOGGER.info("ReportMqtt客户端连接异常，异常信息：" + e);
        }
    }

    @Override
    public void connectionLost(Throwable throwable) {
        try {
            LOGGER.info("程序出现异常，DReportMqtt断线！正在重新连接...:");
            client.close();
            this.result();
            LOGGER.info("ReportMqtt重新连接成功");
        } catch (MqttException e) {
            LOGGER.info(e.getMessage());
        }

    }

    @Override
    public void messageArrived(String topic, MqttMessage mqttMessage) {
        LOGGER.info("接收消息主题:"+ topic);
        LOGGER.info("接收消息Qos:"+ mqttMessage.getQos());
        LOGGER.info("接收消息内容 :"+ new String(mqttMessage.getPayload()));
        String msg = new String(mqttMessage.getPayload());
        try {
            JSONObject jsonObject = JSON.parseObject(msg);
            String clientId = String.valueOf(jsonObject.get("clientid"));
            if (topic.endsWith("disconnected")) {
                LOGGER.info("客户端已掉线：{}",clientId);
            } else {
                LOGGER.info("客户端已上线：{}",clientId);
            }
        } catch (JSONException e) {
            LOGGER.error("JSON Format Parsing Exception : {}", msg);
        }

    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
        LOGGER.info("消息发送成功");
    }
}
