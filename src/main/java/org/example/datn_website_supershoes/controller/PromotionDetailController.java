package org.example.datn_website_supershoes.controller;

import org.example.datn_website_supershoes.model.PromotionDetail;
import org.example.datn_website_supershoes.service.PromotionDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/promotion-detail")
public class PromotionDetailController {

    @Autowired
    private PromotionDetailService promotionDetailService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllPromotionDetails() {
        List<PromotionDetail> listPromotionDetails = promotionDetailService.getAllPromotionDetails();
        Map<String, Object> response = new HashMap<>();
        response.put("DT", listPromotionDetails);
        response.put("EC", 0);
        response.put("EM", "Get all promotion details succeeded");

        return ResponseEntity.ok(response);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<Map<String, Object>> getPromotionDetailById(@PathVariable Long id) {
        Optional<PromotionDetail> promotionDetail = promotionDetailService.getPromotionDetailById(id);
        Map<String, Object> response = new HashMap<>();
        if (promotionDetail.isPresent()) {
            response.put("data", promotionDetail.get());
            response.put("errorCode", 0);
            response.put("message", "PromotionDetail retrieved successfully");
            return ResponseEntity.ok(response);
        } else {
            response.put("errorCode", 1);
            response.put("message", "PromotionDetail not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @PostMapping("/add")
    public ResponseEntity<Map<String, Object>> createPromotionDetail(@RequestBody PromotionDetail promotionDetail) {
        if (promotionDetail == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createErrorResponse("Invalid promotion detail data"));
        }

        PromotionDetail createdPromotionDetail = promotionDetailService.createPromotionDetail(promotionDetail);
        Map<String, Object> response = new HashMap<>();
        response.put("data", createdPromotionDetail);
        response.put("errorCode", 0);
        response.put("message", "PromotionDetail added successfully");

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Map<String, Object>> updatePromotionDetail(@PathVariable Long id, @RequestBody PromotionDetail promotionDetail) {
        try {
            PromotionDetail updatedPromotionDetail = promotionDetailService.updatePromotionDetail(id, promotionDetail);
            Map<String, Object> response = new HashMap<>();
            response.put("data", updatedPromotionDetail);
            response.put("errorCode", 0);
            response.put("message", "PromotionDetail updated successfully");

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("errorCode", 1);
            response.put("message", "Error updating promotion detail: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Map<String, Object>> deletePromotionDetail(@PathVariable Long id) {
        try {
            promotionDetailService.deletePromotionDetail(id);
            Map<String, Object> response = new HashMap<>();
            response.put("errorCode", 0);
            response.put("message", "PromotionDetail deleted successfully");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("errorCode", 1);
            response.put("message", "Error deleting promotion detail: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    private Map<String, Object> createErrorResponse(String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("data", null);
        response.put("errorCode", 1);
        response.put("message", message);
        return response;
    }
}
