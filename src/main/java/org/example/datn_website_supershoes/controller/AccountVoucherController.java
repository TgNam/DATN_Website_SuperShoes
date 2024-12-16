package org.example.datn_website_supershoes.controller;

import org.example.datn_website_supershoes.dto.request.EmailRequest;
import org.example.datn_website_supershoes.service.AccountService;
import org.example.datn_website_supershoes.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/account-voucher")
public class AccountVoucherController {

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
}
