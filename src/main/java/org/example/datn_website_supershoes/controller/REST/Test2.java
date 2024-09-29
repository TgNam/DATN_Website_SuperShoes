package org.example.datn_website_supershoes.controller.REST;

import org.example.datn_website_supershoes.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/testEmail")
public class Test2 {
    @Autowired
    private EmailService emailService;
    @GetMapping("/send-email")
    private String setEmail(){
        emailService.sendEmail("nam552502102@gmail.com","Mật khẩu của tài khoản nam060720041220@gmail.com","MK của bạn là :552502102");
        return "Email được gửi thành công !";
    }

}
