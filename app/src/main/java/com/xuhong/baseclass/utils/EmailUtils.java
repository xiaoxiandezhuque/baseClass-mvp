package com.xuhong.baseclass.utils;


import java.io.File;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

/**
 * Created by BHKJ on 2016/9/20.
 */

public class EmailUtils {

    /**
     * 邮件发送程序
     *
     * @param subject 邮件主题
     * @param content 邮件内容
     * @throws Exception
     * @throws MessagingException
     */
    public static void sendEmail( String subject, String content) throws Exception, MessagingException {
        String host = "smtp.qq.com";
        String address = "962139864@qq.com";
        String from = "962139864@qq.com";
        String password = "pqywzqplcpjobcjf";// 密码
//        if ("".equals(to) || to == null) {
        String    to = "962139864@qq.com";
//        }
        String port = "25";
        SendEmail(host, address, from, password, to, port, subject, content);
    }

    /**
     * 邮件发送程序
     *
     * @param host     邮件服务器 如：smtp.qq.com
     * @param address  发送邮件的地址 如：545099227@qq.com
     * @param from     来自： wsx2miao@qq.com
     * @param password 您的邮箱密码
     * @param to       接收人
     * @param port     端口（QQ:25）
     * @param subject  邮件主题
     * @param content  邮件内容
     * @throws Exception
     */
    public static void SendEmail(String host, String address, String from, String password, String to, String port, String subject, String content) throws Exception {
        Multipart multiPart;
        String finalString = "";

        Properties props = System.getProperties();
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.user", address);
        props.put("mail.smtp.password", password);
        props.put("mail.smtp.port", port);
        props.put("mail.smtp.auth", "true");

        Session session = Session.getDefaultInstance(props, null);
        DataHandler handler = new DataHandler(new ByteArrayDataSource(finalString.getBytes(), "text/plain"));
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(from));
        message.setDataHandler(handler);


        multiPart = new MimeMultipart();
        InternetAddress toAddress;
        toAddress = new InternetAddress(to);
        message.addRecipient(Message.RecipientType.TO, toAddress);

        message.setSubject(subject);
        message.setContent(multiPart);
        message.setText(content);


        Transport transport = session.getTransport("smtp");
        transport.connect(host, address, password);
        transport.sendMessage(message, message.getAllRecipients());
        transport.close();

    }

    public static void sendEmailPic(String subject, String content, List<String> mData) throws Exception, MessagingException {


        String host = "smtp.qq.com";
        String address = "962139864@qq.com";
        String from = "962139864@qq.com";
        String password = "pqywzqplcpjobcjf";// 密码
//        if ("".equals(to) || to == null) {
        String    to = "962139864@qq.com";
//        }
        String port = "25";

        String finalString = "";
        int  count = mData.size()/100+1;
        for (int i = 0; i <count ; i++) {
            Properties props = System.getProperties();
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", host);
            props.put("mail.smtp.user", address);
            props.put("mail.smtp.password", password);
            props.put("mail.smtp.port", port);
            props.put("mail.smtp.auth", "true");

            Session session = Session.getDefaultInstance(props,null);
            DataHandler handler = new DataHandler(new ByteArrayDataSource(finalString.getBytes(), "text/plain"));
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setDataHandler(handler);


            MimeMultipart mixedMultipart = new MimeMultipart();

            for (int j = 0; j <100 ; j++) {
                MimeBodyPart body_pic = new MimeBodyPart();
                DataHandler picDataHandler = new DataHandler(new FileDataSource(
                        new File(Bimp.revitionImageSize(mData.get(100*i+j)))));
                body_pic.setDataHandler(picDataHandler);
                body_pic.setContentID(i*100+j+"");
                body_pic.setFileName(""+(i*100+j)+".jpg");
                mixedMultipart.addBodyPart(body_pic);//组合到一起
            }

            mixedMultipart.setSubType("mixed");


            InternetAddress toAddress;
            toAddress = new InternetAddress(to);
            message.addRecipient(Message.RecipientType.TO, toAddress);

            message.setSubject(subject);
            message.setContent(mixedMultipart);


            Transport transport = session.getTransport("smtp");
            transport.connect(host, address, password);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();

        }



    }

}
