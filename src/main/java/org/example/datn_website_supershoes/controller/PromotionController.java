package org.example.datn_website_supershoes.controller;

import org.example.datn_website_supershoes.dto.response.PromotionResponse;
import org.example.datn_website_supershoes.model.Promotion;
import org.example.datn_website_supershoes.service.PromotionService;
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
@RequestMapping("/promotion")
public class PromotionController {

    @Autowired
    private PromotionService promotionService;

    @GetMapping("/list-promotion")
    public ResponseEntity<Map<String, Object>> getAllPromotions() {
        List<PromotionResponse> listPromotions = promotionService.getAllPromotions();
        Map<String, Object> response = new HashMap<>();
        response.put("DT", listPromotions);
        response.put("EC", 0);
        response.put("EM", "Get all promotions succeeded");

        return ResponseEntity.ok(response);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<Map<String, Object>> getPromotionById(@PathVariable Long id) {
        Optional<Promotion> promotion = promotionService.getPromotionById(id);
        Map<String, Object> response = new HashMap<>();
        if (promotion.isPresent()) {
            response.put("data", promotion.get());
            response.put("errorCode", 0);
            response.put("message", "Promotion retrieved successfully");
            return ResponseEntity.ok(response);
        } else {
            response.put("errorCode", 1);
            response.put("message", "Promotion not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @PostMapping("/add")
    public ResponseEntity<Map<String, Object>> createPromotion(@RequestBody Promotion promotion) {
        if (promotion == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createErrorResponse("Invalid promotion data"));
        }

        Promotion createdPromotion = promotionService.createPromotion(promotion);
        Map<String, Object> response = new HashMap<>();
        response.put("data", createdPromotion);
        response.put("errorCode", 0);
        response.put("message", "Promotion added successfully");

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Map<String, Object>> updatePromotion(@PathVariable Long id, @RequestBody Promotion promotionDetails) {
        try {
            Promotion updatedPromotion = promotionService.updatePromotion(id, promotionDetails);
            Map<String, Object> response = new HashMap<>();
            response.put("data", updatedPromotion);
            response.put("errorCode", 0);
            response.put("message", "Promotion updated successfully");

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("errorCode", 1);
            response.put("message", "Error updating promotion: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Map<String, Object>> deletePromotion(@PathVariable Long id) {
        try {
            promotionService.deletePromotion(id);
            Map<String, Object> response = new HashMap<>();
            response.put("errorCode", 0);
            response.put("message", "Promotion deleted successfully");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("errorCode", 1);
            response.put("message", "Error deleting promotion: " + e.getMessage());
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
