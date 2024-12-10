package org.example.datn_website_supershoes.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import java.util.List;

@Service
public class EmailService {

    @Autowired
    JavaMailSender mailSender;

    public void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }

    public void sendEmailVoucher(List<String> to, String subject, String body) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();

        // true indicates multipart message (for HTML)
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(to.toArray(new String[0]));
        helper.setSubject(subject);
        helper.setText(body, true);  // second argument true means the body is HTML

        // Send the email
        mailSender.send(message);
    }

}
