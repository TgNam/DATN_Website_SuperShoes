package org.example.datn_website_supershoes.service;

import org.example.datn_website_supershoes.Enum.Status;
import org.example.datn_website_supershoes.dto.request.AccountRequest;
import org.example.datn_website_supershoes.dto.request.PromotionCreationRequest;
import org.example.datn_website_supershoes.dto.request.PromotionDetailRequest;
import org.example.datn_website_supershoes.dto.request.PromotionRequest;
import org.example.datn_website_supershoes.dto.response.PromotionResponse;
import org.example.datn_website_supershoes.model.Account;
import org.example.datn_website_supershoes.model.Promotion;
import org.example.datn_website_supershoes.model.PromotionDetail;
import org.example.datn_website_supershoes.repository.PromotionDetailRepository;
import org.example.datn_website_supershoes.repository.PromotionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;


@Service
public class PromotionService {

    @Autowired
    private PromotionRepository promotionRepository;
    @Autowired
    private PromotionDetailService promotionDetailService;
    @Autowired
    private RandomPasswordGeneratorService randomCodePromotion;

    @Transactional
    public void updateExpiredDiscounts() {
        LocalDateTime now = LocalDateTime.now();
        List<Promotion> expiredDiscounts = promotionRepository.findExpiredDiscounts(now);
        for (Promotion discount : expiredDiscounts) {
            discount.setStatus("EXPIRED");
        }
        promotionRepository.saveAll(expiredDiscounts);
    }

    public List<PromotionResponse> getAllPromotion() {
        return promotionRepository.listPromotionResponse();
    }

    public Promotion createPromotion(PromotionCreationRequest promotionCreationRequest) {
        System.out.println("Check Ngày bắt đầu: " + promotionCreationRequest.getPromotionRequest().getStartAt());
        System.out.println("Check Ngày kết thúc: " + promotionCreationRequest.getPromotionRequest().getEndAt());
        promotionCreationRequest.getPromotionRequest().validateEndDates();
        Promotion promotion = promotionRepository.save(convertPromotionRequestDTO(promotionCreationRequest.getPromotionRequest()));
        if (promotionCreationRequest.getPromotionDetailRequest() != null && !promotionCreationRequest.getPromotionDetailRequest().isEmpty()) {
            List<PromotionDetail> promotionDetails = promotionDetailService.createPromotionDetail(promotion, promotionCreationRequest.getPromotionDetailRequest());
        }

        return promotion;
    }

    private Promotion convertPromotionRequestDTO(PromotionRequest promotionRequest) {
        String codePromotion = generatePromotionCode();
        Promotion promotion = Promotion.builder()
                .codePromotion(codePromotion)
                .name(promotionRequest.getName())
                .note(promotionRequest.getNote())
                .value(promotionRequest.getValue())
                .type(promotionRequest.getType())
                .startAt(promotionRequest.getStartAt())
                .endAt(promotionRequest.getEndAt())
                .build();
        promotion.setStatus(Status.UPCOMING.toString());
        return promotion;
    }

    private String generatePromotionCode() {
        return "PROMO" + randomCodePromotion.getCodePromotion();
    }

}
