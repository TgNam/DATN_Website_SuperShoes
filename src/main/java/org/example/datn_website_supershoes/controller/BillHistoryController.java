package org.example.datn_website_supershoes.controller;

import org.example.datn_website_supershoes.dto.request.BillHistoryRequest;
import org.example.datn_website_supershoes.dto.response.BillHistoryResponse;
import org.example.datn_website_supershoes.service.BillHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/bill-history")
public class BillHistoryController {

    @Autowired
    private BillHistoryService billHistoryService;

    @GetMapping("/viewHistory/{codeBill}")
    public ResponseEntity<Map<String, Object>> getBillHistoryByBillCode(@PathVariable String codeBill) {
        List<BillHistoryResponse> billHistoryList = billHistoryService.getBillHistoryByBillCode(codeBill);
        Map<String, Object> response = new HashMap<>();
        response.put("DT", billHistoryList);
        response.put("EC", 0);
        response.put("EM", "Get BillHistory by BillCode successfully");
        return ResponseEntity.ok(response);
    }

    // Create a new BillHistory
    @PostMapping("/add")
    public ResponseEntity<Map<String, Object>> createBillHistory(@RequestBody BillHistoryRequest billHistoryRequest) {
        BillHistoryResponse createdBillHistory = billHistoryService.createBillHistory(billHistoryRequest);
        Map<String, Object> response = new HashMap<>();
        response.put("DT", createdBillHistory);
        response.put("EC", 0);
        response.put("EM", "BillHistory created successfully");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Get all BillHistory entries for a specific Bill
    @GetMapping("/by-bill/{billId}")
    public ResponseEntity<Map<String, Object>> getBillHistoryByBillId(@PathVariable Long billId) {
        List<BillHistoryResponse> billHistoryList = billHistoryService.getBillHistoryByBillId(billId);
        Map<String, Object> response = new HashMap<>();
        response.put("DT", billHistoryList);
        response.put("EC", 0);
        response.put("EM", "Get BillHistory by BillId successfully");
        return ResponseEntity.ok(response);
    }

    // Get all BillHistory entries for a specific Account
    @GetMapping("/by-account/{accountId}")
    public ResponseEntity<Map<String, Object>> getBillHistoryByAccountId(@PathVariable Long accountId) {
        List<BillHistoryResponse> billHistoryList = billHistoryService.getBillHistoryByAccountId(accountId);
        Map<String, Object> response = new HashMap<>();
        response.put("DT", billHistoryList);
        response.put("EC", 0);
        response.put("EM", "Get BillHistory by AccountId successfully");
        return ResponseEntity.ok(response);
    }

    // Update a BillHistory entry
    @PutMapping("/update/{id}")
    public ResponseEntity<Map<String, Object>> updateBillHistory(@PathVariable Long id, @RequestBody BillHistoryRequest billHistoryRequest) {
        BillHistoryResponse updatedBillHistory = billHistoryService.updateBillHistory(id, billHistoryRequest);
        Map<String, Object> response = new HashMap<>();
        response.put("DT", updatedBillHistory);
        response.put("EC", 0);
        response.put("EM", "BillHistory updated successfully");
        return ResponseEntity.ok(response);
    }

    // Delete a BillHistory entry
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Map<String, Object>> deleteBillHistory(@PathVariable Long id) {
        billHistoryService.deleteBillHistory(id);
        Map<String, Object> response = new HashMap<>();
        response.put("EC", 0);
        response.put("EM", "BillHistory deleted successfully");
        return ResponseEntity.ok(response);
    }
}
