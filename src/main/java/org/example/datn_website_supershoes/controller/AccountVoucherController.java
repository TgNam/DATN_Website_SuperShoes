package org.example.datn_website_supershoes.controller;

import jakarta.validation.constraints.NotNull;
import org.example.datn_website_supershoes.dto.request.AccountVoucherRequest;
import org.example.datn_website_supershoes.dto.request.EmailRequest;
import org.example.datn_website_supershoes.dto.response.AccountVoucherResponse;
import org.example.datn_website_supershoes.dto.response.Response;
import org.example.datn_website_supershoes.service.AccountService;
import org.example.datn_website_supershoes.service.AccountVoucherService;
import org.example.datn_website_supershoes.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/api/v1/account-voucher")
public class AccountVoucherController {

    @Autowired
    AccountVoucherService accountVoucherService;

    @Autowired
    AccountService accountService;

    @Autowired
    EmailService emailService;

    @PostMapping("/emails")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getEmailsByCustomerIds(@RequestBody Map<String, List<Long>> customerIdsMap) {
        // Check if customerIdsMap contains the key "customerIds"
        if (!customerIdsMap.containsKey("customerIds")) {
            return ResponseEntity.badRequest().body("Missing 'customerIds' key in the request body.");
        }

        List<Long> customerIds = customerIdsMap.get("customerIds");

        // Validate if customerIds list is not empty
        if (customerIds == null || customerIds.isEmpty()) {
            return ResponseEntity.badRequest().body("Customer IDs list is empty or null.");
        }

        try {
            List<String> emails = accountService.findEmailsByCustomerIds(customerIds);

            // Check if emails were found
            if (emails == null || emails.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No emails found for the provided customer IDs.");
            }

            return ResponseEntity.ok(Map.of("emails", emails));

        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving emails: " + e.getMessage());
        }
    }

    @PostMapping("/send-email")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> sendEmail(@RequestBody EmailRequest emailRequest) {
        try {
            List<String> recipients;
            if (emailRequest.getTo().contains(",")) {
                recipients = Arrays.asList(emailRequest.getTo().split(","));
            } else {
                recipients = Collections.singletonList(emailRequest.getTo());
            }

            emailService.sendEmailVoucher(recipients, emailRequest.getSubject(), emailRequest.getBody());
            return ResponseEntity.ok("Email sent successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to send email: " + e.getMessage());
        }
    }

    @GetMapping()
    public ResponseEntity<Map<String, Object>> getAllAccountVouchers() {
        List<AccountVoucherResponse> accountVoucherList = accountVoucherService.getAllAccountVouchers();
        Map<String, Object> response = new HashMap<>();
        response.put("DT", accountVoucherList);
        response.put("EC", 0);
        response.put("EM", "Get all account voucher succeed");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
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
    @PreAuthorize("hasRole('ADMIN')")
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

}
