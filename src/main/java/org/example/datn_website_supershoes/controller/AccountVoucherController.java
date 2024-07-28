package org.example.datn_website_supershoes.controller;

import org.example.datn_website_supershoes.model.AccountVoucher;
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
import java.util.Optional;

@RestController
@RequestMapping("/account-voucher")
public class AccountVoucherController {

    @Autowired
    private AccountVoucherService accountVoucherService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllAccountVoucher() {
        List<AccountVoucher> listAccountVouchers = accountVoucherService.getAllAccountVouchers();
        Map<String, Object> response = new HashMap<>();
        response.put("DT", listAccountVouchers);
        response.put("EC", 0);
        response.put("EM", "Get all account vouchers succeed");

        return ResponseEntity.ok(response);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<AccountVoucher> getAccountVoucherById(@PathVariable Long id) {
        Optional<AccountVoucher> accountVoucher = accountVoucherService.getAccountVoucherById(id);
        return accountVoucher.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/add")
    public ResponseEntity<Map<String, Object>> createAccountVoucher(@RequestBody AccountVoucher accountVoucher) {
        AccountVoucher createdAccountVoucher = accountVoucherService.createAccountVoucher(accountVoucher);
        Map<String, Object> response = new HashMap<>();
        response.put("DT", createdAccountVoucher);
        response.put("EC", 0);
        response.put("EM", "AccountVoucher add successful");

        return ResponseEntity.ok(response);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Map<String, Object>> updateAccountVoucher(
            @PathVariable Long id, @RequestBody AccountVoucher accountVoucher) {
        AccountVoucher updatedAccountVoucher = accountVoucherService.updateAccountVoucher(id, accountVoucher);
        Map<String, Object> response = new HashMap<>();
        response.put("DT", updatedAccountVoucher);
        response.put("EC", 0);
        response.put("EM", "AccountVoucher update successful");

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteAccountVoucher(@PathVariable Long id) {
        try {
            accountVoucherService.deleteAccountVoucher(id);
            return ResponseEntity.status(HttpStatus.OK).body("AccountVoucher deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting account voucher");
        }
    }
}
