package org.example.datn_website_supershoes.controller;

import org.example.datn_website_supershoes.dto.response.PayBillResponse;
import org.example.datn_website_supershoes.model.PayBill;
import org.example.datn_website_supershoes.service.PayBillService;
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
@RequestMapping("/pay-bill")
public class PayBillController {

    @Autowired
    private PayBillService payBillService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllPayBills() {
        List<PayBillResponse> listPayBills = payBillService.getAllPayBills();
        Map<String, Object> response = new HashMap<>();
        response.put("DT", listPayBills);
        response.put("EC", 0);
        response.put("EM", "Get all pay bills succeed");

        return ResponseEntity.ok(response);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<PayBill> getPayBillById(@PathVariable Long id) {
        Optional<PayBill> payBill = payBillService.getPayBillById(id);
        return payBill.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/add")
    public ResponseEntity<Map<String, Object>> createPayBill(@RequestBody PayBill payBill) {
        PayBill createdPayBill = payBillService.createPayBill(payBill);
        Map<String, Object> response = new HashMap<>();
        response.put("DT", createdPayBill);
        response.put("EC", 0);
        response.put("EM", "PayBill added successfully");

        return ResponseEntity.ok(response);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Map<String, Object>> updatePayBill(@PathVariable Long id, @RequestBody PayBill payBill) {
        PayBill updatedPayBill = payBillService.updatePayBill(id, payBill);
        Map<String, Object> response = new HashMap<>();
        response.put("DT", updatedPayBill);
        response.put("EC", 0);
        response.put("EM", "PayBill updated successfully");

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deletePayBill(@PathVariable Long id) {
        try {
            payBillService.deletePayBill(id);
            return ResponseEntity.status(HttpStatus.OK).body("PayBill deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting PayBill");
        }
    }
}
