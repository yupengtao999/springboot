//package com.ypt.springboot.WebSocket;
//
//import org.springframework.web.socket.*;
//import org.springframework.web.socket.handler.TextWebSocketHandler;
//
//public class MyHandler extends TextWebSocketHandler {
//    public void handlerTextMessage(WebSocketSession session, TextMessage message){
//        WebSocketHandler webSocketHandler = new WebSocketHandler() {
//            @Override
//            public void afterConnectionEstablished(WebSocketSession webSocketSession) throws Exception {
//
//            }
//
//            @Override
//            public void handleMessage(WebSocketSession webSocketSession, WebSocketMessage<?> webSocketMessage) throws Exception {
//
//            }
//
//            @Override
//            public void handleTransportError(WebSocketSession webSocketSession, Throwable throwable) throws Exception {
//
//            }
//
//            @Override
//            public void afterConnectionClosed(WebSocketSession webSocketSession, CloseStatus closeStatus) throws Exception {
//
//            }
//
//            @Override
//            public boolean supportsPartialMessages() {
//                return false;
//            }
//        };
//    }
//}
