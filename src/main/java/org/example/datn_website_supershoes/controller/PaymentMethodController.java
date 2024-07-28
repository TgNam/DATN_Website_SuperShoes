package org.example.datn_website_supershoes.controller;

import org.example.datn_website_supershoes.model.PaymentMethod;
import org.example.datn_website_supershoes.service.PaymentMethodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/paymentMethod")
public class PaymentMethodController {

    @Autowired
    private PaymentMethodService paymentMethodService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllPaymentMethods() {
        List<PaymentMethod> listPaymentMethods = paymentMethodService.getAllPaymentMethods();
        Map<String, Object> response = new HashMap<>();
        response.put("DT", listPaymentMethods);
        response.put("EC", 0);
        response.put("EM", "Get all payment methods succeed");

        return ResponseEntity.ok(response);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<PaymentMethod> getPaymentMethodById(@PathVariable Long id) {
        Optional<PaymentMethod> paymentMethod = paymentMethodService.getPaymentMethodById(id);
        return paymentMethod.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/add")
    public ResponseEntity<Map<String, Object>> createPaymentMethod(@RequestBody PaymentMethod paymentMethod) {
        PaymentMethod createdPaymentMethod = paymentMethodService.createPaymentMethod(paymentMethod);
        Map<String, Object> response = new HashMap<>();
        response.put("DT", createdPaymentMethod);
        response.put("EC", 0);
        response.put("EM", "PaymentMethod added successfully");

        return ResponseEntity.ok(response);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Map<String, Object>> updatePaymentMethod(@PathVariable Long id, @RequestBody PaymentMethod paymentMethod) {
        PaymentMethod updatedPaymentMethod = paymentMethodService.updatePaymentMethod(id, paymentMethod);
        Map<String, Object> response = new HashMap<>();
        response.put("DT", updatedPaymentMethod);
        response.put("EC", 0);
        response.put("EM", "PaymentMethod updated successfully");

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deletePaymentMethod(@PathVariable Long id) {
        try {
            paymentMethodService.deletePaymentMethod(id);
            return ResponseEntity.status(HttpStatus.OK).body("PaymentMethod deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting PaymentMethod");
        }
    }
}
