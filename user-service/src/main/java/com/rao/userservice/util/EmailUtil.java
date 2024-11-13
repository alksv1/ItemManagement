package com.rao.userservice.util;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class EmailUtil {

    @Autowired
    public EmailUtil(JavaMailSender mailSender, EmailProperties emailProperties) {
        this.mailSender = mailSender;
        this.emailProperties = emailProperties;
    }

    private final JavaMailSender mailSender;

    private final EmailProperties emailProperties;

    public void sendMail(String to, String captcha) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(emailProperties.getFrom()); // 发件人邮箱
        message.setTo(to); // 收件人邮箱
        message.setSubject(emailProperties.getSubject()); // 邮件主题
        message.setText(emailProperties.getContent() + captcha); // 邮件内容

        mailSender.send(message); // 发送邮件
    }
}
