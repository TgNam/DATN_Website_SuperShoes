package org.example.datn_website_supershoes.controller.REST;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class Test {

    @GetMapping
    public String get(){
        return "Succc";
    }
}
