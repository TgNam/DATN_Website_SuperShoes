package org.example.datn_website_supershoes.service;

import org.example.datn_website_supershoes.Enum.Status;
import org.example.datn_website_supershoes.dto.request.LoginRequest;
import org.example.datn_website_supershoes.dto.token.TokenResponse;
import org.example.datn_website_supershoes.model.Account;
import org.example.datn_website_supershoes.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Optional;


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
    @Transactional
    public  Account register(Account account){
        Optional<Account> checkAccount = accountRepository.findByEmail(account.getEmail());
        if(checkAccount.isPresent()){
            throw  new RuntimeException("Email đã được đăng ký!");
        }
        account.setRole("USER");
        account.setStatus(Status.ACTIVE.toString());
        account.setPassword(passwordEncoderService.encodedPassword(account.getPassword()));
        return  accountRepository.save(account);
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
