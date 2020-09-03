package com.ypt.springboot.socket;

import java.net.Socket;

public class Test {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("127.0.0.1", 30000);
            socket.setKeepAlive(true);
            socket.setSoTimeout(10);
            while (true) {
                socket.sendUrgentData(0xFF); // 发送心跳包
                System.out.println("目前处于链接状态！");
                Thread.sleep(3 * 1000);//线程睡眠3秒
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
