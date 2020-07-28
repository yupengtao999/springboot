//package com.ypt.springboot.Config;
//
//import lombok.Getter;
//import lombok.Setter;
//import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.integration.annotation.MessagingGateway;
//import org.springframework.integration.annotation.ServiceActivator;
//import org.springframework.integration.channel.DirectChannel;
//import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
//import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
//import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
//import org.springframework.integration.mqtt.support.MqttHeaders;
//import org.springframework.messaging.MessageChannel;
//import org.springframework.messaging.MessageHandler;
//import org.springframework.messaging.handler.annotation.Header;
//
//@Configuration
//@ConfigurationProperties(prefix = "mqtt1")
//@Getter
//@Setter
//public class MqttSenderConfig {
//    private String username;
//    private String password;
//    private String hostUrl;
//    private String clientId;
//    private String topic;
//    private int timeOut;
//    @Bean
//    public MqttConnectOptions getMqttConnectOptions(){
//        MqttConnectOptions mqttConnectOptions=new MqttConnectOptions();
//        mqttConnectOptions.setUserName(username);
//        mqttConnectOptions.setPassword(password.toCharArray());
//        mqttConnectOptions.setServerURIs(new String[]{hostUrl});
//        mqttConnectOptions.setKeepAliveInterval(2);
//        return mqttConnectOptions;
//    }
//    @Bean
//    public MqttPahoClientFactory mqttClientFactory() {
//        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
//        factory.setConnectionOptions(getMqttConnectOptions());
//        return factory;
//    }
//    @Bean
//    @ServiceActivator(inputChannel = "mqttOutboundChannel")
//    public MessageHandler mqttOutbound() {
//        MqttPahoMessageHandler messageHandler =  new MqttPahoMessageHandler(clientId, mqttClientFactory());
//        messageHandler.setAsync(true);
//        messageHandler.setDefaultTopic(topic);
//        return messageHandler;
//    }
//    @Bean
//    public MessageChannel mqttOutboundChannel() {
//        return new DirectChannel();
//    }
//
//}
