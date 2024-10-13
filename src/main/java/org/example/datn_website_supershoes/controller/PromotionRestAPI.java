package org.example.datn_website_supershoes.controller;

import jakarta.validation.Valid;
import org.example.datn_website_supershoes.dto.request.AccountRequest;
import org.example.datn_website_supershoes.dto.request.PromotionCreationRequest;
import org.example.datn_website_supershoes.dto.request.PromotionDetailRequest;
import org.example.datn_website_supershoes.dto.request.PromotionRequest;
import org.example.datn_website_supershoes.dto.response.PromotionResponse;
import org.example.datn_website_supershoes.dto.response.Response;
import org.example.datn_website_supershoes.model.Account;
import org.example.datn_website_supershoes.model.Promotion;
import org.example.datn_website_supershoes.service.PromotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/promotion")
public class PromotionRestAPI {

    @Autowired
    private PromotionService promotionService;
    @GetMapping("/listPromotion")
    public List<PromotionResponse> getAllPromotion(){
        return promotionService.getAllPromotion();
    }
    @GetMapping("/listSearchPromotion")
    public List<PromotionResponse> getAllPromotionSearch(
            @RequestParam("search") String search,
            @RequestParam("status") String status) {

        String searchLower = search.trim().toLowerCase();
        return promotionService.getAllPromotion().stream()
                .filter(promotion -> {
                    String promotionName = promotion.getName().toLowerCase();
                    String codePromotion = promotion.getCodePromotion().toLowerCase();
                    return promotionName.contains(searchLower) || codePromotion.contains(searchLower);
                })
                .filter(promotion -> promotion.getStatus().toLowerCase().contains(status.trim().toLowerCase()))
                .collect(Collectors.toList());
    }
    @PostMapping("/createPromotion")
    public ResponseEntity<?> createAccount(
            @RequestBody @Valid PromotionCreationRequest promotionCreationRequest,
            BindingResult result
    ) {
        try {
            if (result.hasErrors()) {
                List<String> errors = result.getAllErrors().stream()
                        .map(error -> error.getDefaultMessage())
                        .collect(Collectors.toList());
                return ResponseEntity.badRequest().body(errors);
            }
            Promotion promotion = promotionService.createPromotion(promotionCreationRequest);
            return ResponseEntity.ok(promotion);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
