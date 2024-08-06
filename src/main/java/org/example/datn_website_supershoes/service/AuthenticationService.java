package org.example.datn_website_supershoes.service;

import org.example.datn_website_supershoes.dto.request.AccountRequest;
import org.example.datn_website_supershoes.dto.token.TokenResponse;
import org.example.datn_website_supershoes.model.Account;
import org.example.datn_website_supershoes.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    @Autowired
    AccountRepository accountRepository;
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JWTService jwtService;

    public TokenResponse authentication(AccountRequest accountRequest){
        Account account = accountRepository
                .findByEmail(accountRequest.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Not found account!"));
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(accountRequest.getEmail(),accountRequest.getPassword())
        );

        return TokenResponse.builder()
                .accessToken(jwtService.generateToken(account))
                .resfreshToken("1234")
                .build();

    }
}
