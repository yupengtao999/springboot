package com.ypt.springboot.bean;

import com.sun.mail.util.MailSSLSocketFactory;
import org.springframework.messaging.MessagingException;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.security.GeneralSecurityException;
import java.util.Properties;

public class SendEmail {
    public static void main(String[] args) throws MessagingException, GeneralSecurityException, javax.mail.MessagingException {
        //创建一个配置文件并保存
        Properties properties = new Properties();

        properties.setProperty("mail.host","smtp.qq.com");

        properties.setProperty("mail.transport.protocol","smtp");

        properties.setProperty("mail.smtp.auth","true");


        //QQ存在一个特性设置SSL加密
        MailSSLSocketFactory sf = new MailSSLSocketFactory();
        sf.setTrustAllHosts(true);
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.ssl.socketFactory", sf);

        //创建一个session对象
        Session session = Session.getDefaultInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("985592648@qq.com","usdefxavvqulbbhb");
            }
        });

        //开启debug模式
        session.setDebug(true);

        //获取连接对象
        Transport transport = session.getTransport();

        //连接服务器
        transport.connect("smtp.qq.com","985592648@qq.com","16位授权码");

        //创建邮件对象
        MimeMessage mimeMessage = new MimeMessage(session);

        //邮件发送人
        mimeMessage.setFrom(new InternetAddress("985592648@qq.com"));

        //邮件接收人
        mimeMessage.setRecipient(Message.RecipientType.TO,new InternetAddress("yupengtao@126.com"));

        //邮件标题
        mimeMessage.setSubject("你好");

        //邮件内容
        mimeMessage.setContent("我的想法是把代码放进一个循环里","text/html;charset=UTF-8");

        //发送邮件
        transport.sendMessage(mimeMessage,mimeMessage.getAllRecipients());

        //关闭连接
        transport.close();
    }

}
