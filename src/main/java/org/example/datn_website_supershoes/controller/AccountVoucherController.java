package org.example.datn_website_supershoes.controller;

import jakarta.validation.constraints.NotNull;
import org.example.datn_website_supershoes.dto.request.AccountVoucherRequest;
import org.example.datn_website_supershoes.dto.response.Response;
import org.example.datn_website_supershoes.service.AccountVoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/account-voucher")
public class AccountVoucherController {

    @Autowired
    private AccountVoucherService accountVoucherService;

    @GetMapping()
    public ResponseEntity<Map<String, Object>> getAllAccountVouchers() {
        List<AccountVoucherRequest> accountVoucherList = accountVoucherService.getAllAccountVouchers();
        Map<String, Object> response = new HashMap<>();
        response.put("DT", accountVoucherList);
        response.put("EC", 0);
        response.put("EM", "Get all account voucher succeed");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createAccountVoucher(@RequestBody @NotNull AccountVoucherRequest accountVoucherRequest) {
        try {
            return ResponseEntity.ok(accountVoucherService.createAccountVoucher(accountVoucherRequest));
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

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateAccountVoucher(@PathVariable("id") long id,
                                                  @RequestBody AccountVoucherRequest accountVoucherRequest) {
        try {
            return ResponseEntity.ok(accountVoucherService.updateAccountVoucher(id, accountVoucherRequest));
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
    public ResponseEntity<?> deleteAccountVoucher(@PathVariable("id") Long id) {
        try {
            accountVoucherService.deleteAccountVoucher(id);
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
}
