//package com.ypt.springboot.Config;
//
//import lombok.Getter;
//import lombok.Setter;
//import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.integration.annotation.ServiceActivator;
//import org.springframework.integration.channel.DirectChannel;
//import org.springframework.integration.core.MessageProducer;
//import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
//import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
//import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
//import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
//import org.springframework.messaging.Message;
//import org.springframework.messaging.MessageChannel;
//import org.springframework.messaging.MessageHandler;
//import org.springframework.messaging.MessagingException;
//
//@Configuration
//@ConfigurationProperties(prefix = "mqtt")
//@Getter
//@Setter
//public class MqttReceiveConfig {
//    private String username;
//    private String password;
//    private String hostUrl;
//    private String clientId;
//    private String topic;
//    private int timeOut;
//    @Bean
//    public MqttConnectOptions getMqttConnectOptions1(){
//        MqttConnectOptions mqttConnectOptions=new MqttConnectOptions();
//        mqttConnectOptions.setUserName(username);
//        mqttConnectOptions.setPassword(password.toCharArray());
//        mqttConnectOptions.setServerURIs(new String[]{hostUrl});
//        mqttConnectOptions.setKeepAliveInterval(2);
//        return mqttConnectOptions;
//    }
//    @Bean
//    public MqttPahoClientFactory mqttClientFactory1() {
//        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
//        factory.setConnectionOptions(getMqttConnectOptions1());
//        return factory;
//    }
//    //接收通道
//    @Bean
//    public MessageChannel mqttInputChannel() {
//        return new DirectChannel();
//    }
//    //配置client,监听的topic
//    @Bean
//    public MessageProducer inbound() {
//        MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(
//                clientId+"_inbound", mqttClientFactory1(), "hello","hello1");
//        adapter.setCompletionTimeout(timeOut);
//        adapter.setConverter(new DefaultPahoMessageConverter());
//        adapter.setQos(1);
//        adapter.setOutputChannel(mqttInputChannel());
//        return adapter;
//    }
//    //通过通道获取数据
//    @Bean
//    @ServiceActivator(inputChannel = "mqttInputChannel")
//    public MessageHandler handler() {
//        return new MessageHandler() {
//            @Override
//            public void handleMessage(Message<?> message) throws MessagingException {
//                String topic = message.getHeaders().get("mqtt_receivedTopic").toString();
//                String type = topic.substring(topic.lastIndexOf("/")+1, topic.length());
//                if("hello".equalsIgnoreCase(topic)){
//                    System.out.println("hello,fuckXX,"+message.getPayload().toString());
//                }else if("hello1".equalsIgnoreCase(topic)){
//                    System.out.println("hello1,fuckXX,"+message.getPayload().toString());
//                }
//            }
//        };
//    }
//    //接收通道
//    @Bean
//    public MessageChannel mqttInputChannelTwo() {
//        return new DirectChannel();
//    }
//    //配置client,监听的topic
//    @Bean
//    public MessageProducer inboundTwo() {
//        MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(
//                clientId+"_inboundTwo", mqttClientFactory1(), "hello2","hello3");
//        adapter.setCompletionTimeout(timeOut);
//        adapter.setConverter(new DefaultPahoMessageConverter());
//        adapter.setQos(1);
//        adapter.setOutputChannel(mqttInputChannelTwo());
//        return adapter;
//    }
//    //通过通道获取数据
//    @Bean
//    @ServiceActivator(inputChannel = "mqttInputChannelTwo")
//    public MessageHandler handlerTwo() {
//        return new MessageHandler() {
//            @Override
//            public void handleMessage(Message<?> message) throws MessagingException {
//                String topic = message.getHeaders().get("mqtt_receivedTopic").toString();
//                String type = topic.substring(topic.lastIndexOf("/")+1, topic.length());
//                if("hello2".equalsIgnoreCase(topic)){
//                    System.out.println("hello2,"+message.getPayload().toString());
//                }else if("hello3".equalsIgnoreCase(topic)){
//                    System.out.println("hello3,"+message.getPayload().toString());
//                }
//            }
//        };
//    }
//
//}
