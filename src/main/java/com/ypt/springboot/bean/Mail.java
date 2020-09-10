package com.ypt.springboot.bean;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class Mail implements Serializable {

    public static final String ENCODEING = "UTF-8";
    private static final long serialVersionUID = 3942874082787679716L;

    private String host; // 服务器地址

    private String sender; // 发件人的邮箱

    private String receiver; // 收件人的邮箱

    private String name; // 发件人昵称

    private String username; // 账号

    private String password; // 密码

    private String subject; // 主题

    private String message; // 信息(支持HTML)

    public static String getENCODEING() {
        return ENCODEING;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static void main(String[] args) {
//        String ip = null;
//        String host = null;
//        try {
//            InetAddress ia = InetAddress.getLocalHost();
//            host = ia.getHostName();//获取计算机名字
//            ip = ia.getHostAddress();//获取IP
//        } catch (UnknownHostException e) {
//            e.printStackTrace();
//        }
//        System.out.println(host);
//        System.out.println(ip);
        String a = "dos";
        String regex = "^d";
        boolean flag = Pattern.matches(regex,a);
        System.out.println(flag);
        Map<Integer,String> map = new HashMap<>();
        map.put(1,"1");
        map.put(2,"2");
        for (Map.Entry<Integer,String> m : map.entrySet()){
            System.out.println(m.getKey()+"\t"+m.getValue());
        }
    }
}
