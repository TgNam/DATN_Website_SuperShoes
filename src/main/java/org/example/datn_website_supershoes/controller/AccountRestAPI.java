package org.example.datn_website_supershoes.controller;

import jakarta.validation.Valid;
import org.example.datn_website_supershoes.dto.request.AccountUpdateRequest;
import org.example.datn_website_supershoes.dto.request.AccountRequest;
import org.example.datn_website_supershoes.dto.request.EmployeeCreationRequest;
import org.example.datn_website_supershoes.dto.request.EmployeeUpdateRequest;
import org.example.datn_website_supershoes.dto.response.AccountResponse;
import org.example.datn_website_supershoes.dto.response.Response;
import org.example.datn_website_supershoes.model.Account;
import org.example.datn_website_supershoes.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.stream.Collectors;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/account")
public class AccountRestAPI {
    @Autowired
    AccountService accountService;

    @PostMapping("/create")
    public ResponseEntity<?> createAccount(@RequestBody @Valid AccountRequest accountRequest, BindingResult result) {
        try {
            if (result.hasErrors()) {
                List<String> errors = result.getAllErrors().stream()
                        .map(error -> error.getDefaultMessage())
                        .collect(Collectors.toList());
                return ResponseEntity.badRequest().body(errors);
            }
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

    @PostMapping("/createEmployee")
    public ResponseEntity<?> createEmployee(@RequestBody @Valid EmployeeCreationRequest employeeCreationRequest, BindingResult result) {
        try {
            if (result.hasErrors()) {
                List<String> errors = result.getAllErrors().stream()
                        .map(error -> error.getDefaultMessage())
                        .collect(Collectors.toList());
                return ResponseEntity.badRequest().body(errors);
            }
            System.out.println(employeeCreationRequest.getAccountRequest().getEmail());
            Account account = accountService.createAccountEmployee(employeeCreationRequest);
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

    @PutMapping("/updateEmployee")
    public ResponseEntity<?> updateEmployee(
            @RequestParam(value = "idAccount", required = false) Long idAccount,
            @RequestParam(value = "idAddress", required = false) Long idAddress,
            @RequestBody @Valid EmployeeUpdateRequest employeeUpdateRequest,
            BindingResult result
    ) {
        try {
            // Kiểm tra nếu idAccount bị trống (null)
            if (idAccount == null) {
                return ResponseEntity.badRequest().body(
                        Response.builder()
                                .status(HttpStatus.BAD_REQUEST.toString())
                                .mess("Lỗi: ID tài khoản không được để trống!")
                                .build()
                );
            }
            if (idAddress == null) {
                return ResponseEntity.badRequest().body(
                        Response.builder()
                                .status(HttpStatus.BAD_REQUEST.toString())
                                .mess("Lỗi: ID địa chỉ không được để trống!")
                                .build()
                );
            }
            if (result.hasErrors()) {
                List<String> errors = result.getAllErrors().stream()
                        .map(error -> error.getDefaultMessage())
                        .collect(Collectors.toList());
                return ResponseEntity.badRequest().body(errors);
            }
            Account account = accountService.updateAccountEmployee(idAccount, idAddress, employeeUpdateRequest);
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

    @PutMapping("/updateAccount")
    public ResponseEntity<?> updateAccount(
            @RequestParam(value = "idAccount", required = false) Long idAccount,
            @RequestBody @Valid AccountUpdateRequest accountRequest,
            BindingResult result
    ) {
        try {
            // Kiểm tra nếu idAccount bị trống (null)
            if (idAccount == null) {
                return ResponseEntity.badRequest().body(
                        Response.builder()
                                .status(HttpStatus.BAD_REQUEST.toString())
                                .mess("Lỗi: ID địa chỉ không được để trống!")
                                .build()
                );
            }
            if (result.hasErrors()) {
                List<String> errors = result.getAllErrors().stream()
                        .map(error -> error.getDefaultMessage())
                        .collect(Collectors.toList());
                return ResponseEntity.badRequest().body(errors);
            }
            Account account = accountService.updateAccount(idAccount, accountRequest);
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

    @PutMapping("/updateStatus")
    private ResponseEntity<?> updateStatus(
            @RequestParam(value = "id", required = false) Long id,
            @RequestParam(value = "aBoolean", required = false) boolean aBoolean
    ) {
        try {
            if (id == null) {
                return ResponseEntity.badRequest().body(
                        Response.builder()
                                .status(HttpStatus.BAD_REQUEST.toString())
                                .mess("Lỗi: ID tài khoản không được để trống!")
                                .build()
                );
            }
            Account account = accountService.updateStatus(id, aBoolean);
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

    @GetMapping("/list-accounts-customer")
    public List<AccountResponse> getAllAccount() {
        List<AccountResponse> accountResponses = accountService.getAllAccountCustomerActive();
        return accountResponses;
    }

    @GetMapping("/list-accounts-customer-search")
    public List<AccountResponse> getAllAccountCustomerSearch(
            @RequestParam("search") String search,
            @RequestParam("status") String status) {

        String searchLower = search.trim().toLowerCase();
        return accountService.getAllAccountCustomerActive().stream()
                .filter(account -> {
                    String accountName = account.getName().toLowerCase();
                    String accountPhone = account.getPhoneNumber().toLowerCase();
                    return accountName.contains(searchLower) || accountPhone.contains(searchLower);
                })
                .filter(account -> account.getStatus().toLowerCase().contains(status.trim().toLowerCase()))
                .collect(Collectors.toList());
    }

    @GetMapping("/findAccounts")
    public ResponseEntity<?> findAccounts(@RequestParam(value = "idAccount", required = false) Long idAccount) {
        try {
            if (idAccount == null) {
                return ResponseEntity.badRequest().body(
                        Response.builder()
                                .status(HttpStatus.BAD_REQUEST.toString())
                                .mess("Lỗi: ID của tài khoản không được để trống!")
                                .build()
                );
            }
            return ResponseEntity.ok().body(accountService.findAccountById(idAccount));
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

    @GetMapping("/list-accounts-employee")
    public List<AccountResponse> getAllAccountEmployee() {
        return accountService.getAllAccountEmployeeActive();
    }

    @GetMapping("/list-accounts-employee-search")
    public List<AccountResponse> getAllAccountEmployeeSearch(
            @RequestParam("search") String search,
            @RequestParam("status") String status) {

        String searchLower = search.trim().toLowerCase();
        return accountService.getAllAccountEmployeeActive().stream()
                .filter(account -> {
                    String accountName = account.getName().toLowerCase();
                    String accountPhone = account.getPhoneNumber().toLowerCase();
                    return accountName.contains(searchLower) || accountPhone.contains(searchLower);
                })
                .filter(account -> account.getStatus().toLowerCase().contains(status.trim().toLowerCase()))
                .collect(Collectors.toList());
    }

    @GetMapping("/get-account-login")
    public Account getAccountLogin(){
        return accountService.getUseLogin();
    }

}
