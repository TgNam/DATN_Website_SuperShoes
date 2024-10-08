package org.example.datn_website_supershoes.service;

import org.example.datn_website_supershoes.dto.request.AccountRequest;
import org.example.datn_website_supershoes.dto.request.LoginRequest;
import org.example.datn_website_supershoes.dto.token.TokenResponse;
import org.example.datn_website_supershoes.model.Account;
import org.example.datn_website_supershoes.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


@Service
public class AuthenticationService {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired

    PasswordEncoderService passwordEncoderService;

    @Autowired
    EmailService emailService;

    @Autowired
    JWTService jwtService;

    public TokenResponse authentication(LoginRequest loginRequest){
        Account account = accountRepository
                .findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Not found account!"));
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(),loginRequest.getPassword())
        );

        return TokenResponse.builder()
                .accessToken(jwtService.generateToken(account))
                .resfreshToken("1234")
                .build();

    }


    public void resetPassword(String email){
        Account account = accountRepository
                .findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Not Found Emaill!"));
        if(!account.getRole().equals("ADMIN")){
            String token = jwtService.generateToken(account);
            String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
            String urlSend = baseUrl + "/api/v1/auth/reset" +token + "/" + account.getId();
            emailService.sendEmail(email,"Khôi Phục Mật Khẩu", urlSend);
        }

    }
    public void verify(String token,Long id){
        Account account = accountRepository
                .findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Not Found Emaill!"));
        if(jwtService.isValid(token,account)){
            emailService.sendEmail(account.getEmail(), "New Pass", "888888" );
            account.setPassword(passwordEncoderService.encodedPassword("888888"));
        }
    }
}
