package org.example.datn_website_supershoes.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PasswordEncoderService {

    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public String encodedPassword(String rawPassword){
        return passwordEncoder.encode(rawPassword);
    }
    public boolean matches(String rawPassword, String encodedPassword){
        return passwordEncoder.matches(rawPassword,encodedPassword);
    }

}
