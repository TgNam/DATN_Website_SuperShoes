package org.example.datn_website_supershoes.controller;

import jakarta.validation.Valid;
import org.example.datn_website_supershoes.Enum.Status;
import org.example.datn_website_supershoes.dto.request.PromotionCreationRequest;
import org.example.datn_website_supershoes.dto.request.PromotionUpdatesRequest;
import org.example.datn_website_supershoes.dto.response.PromotionDetailResponse;
import org.example.datn_website_supershoes.dto.response.PromotionResponse;
import org.example.datn_website_supershoes.dto.response.Response;
import org.example.datn_website_supershoes.model.Promotion;
import org.example.datn_website_supershoes.model.PromotionDetail;
import org.example.datn_website_supershoes.service.PromotionDetailService;
import org.example.datn_website_supershoes.service.PromotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/promotion")
public class PromotionRestAPI {

    @Autowired
    private PromotionService promotionService;
    @Autowired
    private PromotionDetailService promotionDetailService;
    @GetMapping("/listPromotion")
    public List<PromotionResponse> getAllPromotion(){
        return promotionService.getAllPromotion();
    }
    @GetMapping("/getPromotionDetailResponse")
    public ResponseEntity<?> getPromotionDetailResponse(
            @RequestParam(value ="idPromotion", required = false) Long idPromotion
    ){
        try {
            if (idPromotion == null) {
                return ResponseEntity.badRequest().body(
                        Response.builder()
                                .status(HttpStatus.BAD_REQUEST.toString())
                                .mess("Lỗi: ID đợt giảm giá không được để trống!")
                                .build()
                );
            }
            PromotionDetailResponse promotionDetailResponse = promotionService.getPromotionDetailResponse(idPromotion);
            return ResponseEntity.ok(promotionDetailResponse);
        }catch (RuntimeException e){
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(Response.builder()
                            .status(HttpStatus.CONFLICT.toString())
                            .mess(e.getMessage())
                            .build()
                    );
        }
    }
    @GetMapping("/getSearchPromotionDetailResponse")
    public ResponseEntity<?> getAllPromotionDetailResponseSearch(
            @RequestParam(value ="idPromotion", required = false) Long idPromotion,
            @RequestParam("search") String search,
            @RequestParam("nameSize") String nameSize,
            @RequestParam("nameColor") String nameColor,
            @RequestParam("priceRange") String priceRange) {
        if (idPromotion == null) {
            return ResponseEntity.badRequest().body(
                    Response.builder()
                            .status(HttpStatus.BAD_REQUEST.toString())
                            .mess("Lỗi: ID đợt giảm giá không được để trống!")
                            .build()
            );
        }
        PromotionDetailResponse promotionDetailResponse = promotionService.getSearchPromotionDetailResponse(idPromotion,search,nameSize,nameColor,priceRange);
        return ResponseEntity.ok(promotionDetailResponse);
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
    public ResponseEntity<?> createPromotion(
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
        } catch (RuntimeException e){
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(Response.builder()
                            .status(HttpStatus.CONFLICT.toString())
                            .mess(e.getMessage())
                            .build()
                    );
        }
    }
    @PutMapping("/updatePromotion")
    public ResponseEntity<?> updatePromotion(
            @RequestBody @Valid PromotionUpdatesRequest promotionUpdatesRequest,
            BindingResult result
    ) {
        try {
            if (result.hasErrors()) {
                List<String> errors = result.getAllErrors().stream()
                        .map(error -> error.getDefaultMessage())
                        .collect(Collectors.toList());
                return ResponseEntity.badRequest().body(errors);
            }
            Promotion promotion = promotionService.updatePromotion(promotionUpdatesRequest);
            return ResponseEntity.ok(promotion);
        } catch (RuntimeException e){
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(Response.builder()
                            .status(HttpStatus.CONFLICT.toString())
                            .mess(e.getMessage())
                            .build()
                    );
        }
    }
    @PutMapping("/updateStatus")
    private ResponseEntity<?> updateStatus(
            @RequestParam(value ="id", required = false) Long id,
            @RequestParam(value ="aBoolean", required = false) boolean aBoolean
    ){
        try{
            if (id == null) {
                return ResponseEntity.badRequest().body(
                        Response.builder()
                                .status(HttpStatus.BAD_REQUEST.toString())
                                .mess("Lỗi: ID đợt giảm giá không được để trống!")
                                .build()
                );
            }
            Promotion promotion = promotionService.updateStatus(id,aBoolean);
            return ResponseEntity.ok(promotion);
        }catch (RuntimeException e){
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(Response.builder()
                            .status(HttpStatus.CONFLICT.toString())
                            .mess(e.getMessage())
                            .build()
                    );
        }
    }

    @PostMapping("/get-by-product-details")
    private ResponseEntity<?> findPromotionDetailByIdProductDetailAndStatus (@RequestBody List<Long> ids){
        try{
            if (ids == null) {
                return ResponseEntity.badRequest().body(
                        Response.builder()
                                .status(HttpStatus.BAD_REQUEST.toString())
                                .mess("Lỗi: ID sản phẩm không được để trống!")
                                .build()
                );
            }
            return ResponseEntity.ok(promotionDetailService
                    .findPromotionDetailByIdProductDetail(ids));
        }catch (RuntimeException e){
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(Response.builder()
                            .status(HttpStatus.CONFLICT.toString())
                            .mess(e.getMessage())
                            .build()
                    );
        }
    }


}
