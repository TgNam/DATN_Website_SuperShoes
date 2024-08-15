package org.example.datn_website_supershoes.controller;

import org.example.datn_website_supershoes.dto.response.BillDetailResponse;
import org.example.datn_website_supershoes.model.BillDetail;
import org.example.datn_website_supershoes.service.BillDetailService;
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
@RequestMapping("/bill-detail")
public class BillDetailController {

    @Autowired
    private BillDetailService billDetailService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllBillDetails() {
        List<BillDetailResponse> listBillDetails = billDetailService.getAllBillDetails();
        Map<String, Object> response = new HashMap<>();
        response.put("DT", listBillDetails);
        response.put("EC", 0);
        response.put("EM", "Get all bill details succeed");

        return ResponseEntity.ok(response);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<BillDetail> getBillDetailById(@PathVariable Long id) {
        Optional<BillDetail> billDetail = billDetailService.getBillDetailById(id);
        return billDetail.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/add")
    public ResponseEntity<Map<String, Object>> createBillDetail(@RequestBody BillDetail billDetail) {
        BillDetail createdBillDetail = billDetailService.createBillDetail(billDetail);
        Map<String, Object> response = new HashMap<>();
        response.put("DT", createdBillDetail);
        response.put("EC", 0);
        response.put("EM", "BillDetail added successfully");

        return ResponseEntity.ok(response);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Map<String, Object>> updateBillDetail(@PathVariable Long id, @RequestBody BillDetail billDetail) {
        BillDetail updatedBillDetail = billDetailService.updateBillDetail(id, billDetail);
        Map<String, Object> response = new HashMap<>();
        response.put("DT", updatedBillDetail);
        response.put("EC", 0);
        response.put("EM", "BillDetail updated successfully");

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteBillDetail(@PathVariable Long id) {
        try {
            billDetailService.deleteBillDetail(id);
            return ResponseEntity.status(HttpStatus.OK).body("BillDetail deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting BillDetail");
        }
    }
}
