package org.example.datn_website_supershoes.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.example.datn_website_supershoes.dto.request.AccountRequest;
import org.example.datn_website_supershoes.dto.response.AccountResponse;
import org.example.datn_website_supershoes.dto.response.Response;
import org.example.datn_website_supershoes.model.Account;
import org.example.datn_website_supershoes.service.AccountService;
import org.example.datn_website_supershoes.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/account")
public class AccountRestAPI {
    @Autowired
    AccountService accountService;

    @Autowired
    private EmailService emailService;
    @PostMapping("/create")
    public ResponseEntity<?> createAccount(@RequestBody @Valid AccountRequest accountRequest, BindingResult result) {
        if (result.hasErrors()) {
            List<String> errors = result.getAllErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errors);
        }
        try {
            Account account = accountService.createAccount(accountRequest);
            return ResponseEntity.ok(account);
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(Response.builder()
                            .status(HttpStatus.CONFLICT.toString())
                            .mess(e.getMessage())
                            .build()
                    );
        }
    }

    @PutMapping("update/{id}")
    public ResponseEntity<?> updateAccount(@PathVariable("id") long id, @RequestBody AccountRequest accountRequest) {
        try {
            String username = accountRequest.getName();
            Account account = accountService.updateAccount(id, username);
            return ResponseEntity.ok(account);
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Response.builder()
                            .status(HttpStatus.NOT_FOUND.toString())
                            .mess(e.getMessage())
                            .build()
                    );
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        try {
            accountService.deleteAccount(id);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(Response.builder()
                            .status(HttpStatus.OK.toString())
                            .mess("Delete success")
                            .build()
                    );
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Response.builder()
                            .status(HttpStatus.NOT_FOUND.toString())
                            .mess(e.getMessage())
                            .build()
                    );
        }
    }

    @GetMapping("/list-accounts-customer")
    public List<AccountResponse> getAllAccount() {
        return accountService.getAllAccountCustomerActive();
    }
    @GetMapping("/list-accounts-customer-search")
    public List<AccountResponse> getAllAccountSearch(
            @RequestParam("search") String search,
            @RequestParam("status") String status) {
        return accountService.getAllAccountCustomerActive().stream()
                .filter(account -> account.getName().toLowerCase().contains(search.trim().toLowerCase()))
                .filter(account -> "ALL".equalsIgnoreCase(status.trim()) || account.getStatus().equalsIgnoreCase(status.trim()))  // Không lọc theo status nếu là ALL
                .collect(Collectors.toList());
    }

    @GetMapping("/list-accounts-employee")
    public List<AccountResponse> getAllAccountEmployee() {
        return accountService.getAllAccountEmployeeActive();
    }
}
