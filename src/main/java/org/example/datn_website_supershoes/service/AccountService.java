package org.example.datn_website_supershoes.service;

import org.example.datn_website_supershoes.Enum.Status;
import org.example.datn_website_supershoes.dto.request.AccountRequest;
import org.example.datn_website_supershoes.model.Account;
import org.example.datn_website_supershoes.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AccountService {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    PasswordEncoderService passwordEncoderService;

    public Account createAccount(AccountRequest accountRequest){
            Optional<Account> accountOP = accountRepository.findByEmail(accountRequest.getEmail());
            if(accountOP.isPresent()){
                throw new RuntimeException("Email already exists: " + accountRequest.getEmail());
            }
            return accountRepository.save(convertAccountRequestDTO(accountRequest));
    }

    public Account updateAccount(Long id, String username){
        Account account = accountRepository.findById(id).orElseGet( () -> {
            throw new RuntimeException("Account not exits");
        });
        account.setName(username);
        return accountRepository.save(account);
    }

    public void deleteAccount(Long id){
        Account account = accountRepository.findById(id)
                .orElseThrow( () -> new RuntimeException("Account not exits"));
        account.setStatus(Status.INACTIVE.toString());
        accountRepository.deleteById(id);
    }

    public List<Account> getAllAccountActive(){
        return accountRepository.findAllByStatus(Status.ACTIVE.toString());
    }

    public Account  convertAccountRequestDTO(AccountRequest accountRequest){
        Account account = Account.builder()
                .name(accountRequest.getName())
                .email(accountRequest.getEmail())
                .password(passwordEncoderService.encodedPassword(accountRequest.getPassword()))
                .role(accountRequest.getRole())
                .build();
        account.setStatus(Status.ACTIVE.toString());
        return account;
    }

}
