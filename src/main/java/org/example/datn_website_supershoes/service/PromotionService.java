package org.example.datn_website_supershoes.service;

import org.example.datn_website_supershoes.Enum.Status;
import org.example.datn_website_supershoes.dto.request.PromotionRequest;
import org.example.datn_website_supershoes.dto.response.PromotionResponse;
import org.example.datn_website_supershoes.model.Promotion;
import org.example.datn_website_supershoes.repository.PromotionRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class PromotionService {

    @Autowired
    private PromotionRepository promotionRepository;

    public Page<PromotionResponse> getPromotions(Specification<Promotion> spec, Pageable pageable) {
        return promotionRepository.findAll(spec, pageable).map(this::convertToPromotionResponse);
    }

    public Promotion createPromotion(PromotionRequest promotionRequest) {
        Promotion promotion = convertPromotionRequestDTO(promotionRequest);
        promotion.setStatus(Status.ONGOING.toString());
        return promotionRepository.save(promotion);
    }

    public Promotion updatePromotion(Long id, PromotionRequest promotionRequest) {
        Promotion promotion = promotionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Promotion not found"));

        String[] ignoredProperties = {"id", "createdAt", "createdBy", "status"};
        BeanUtils.copyProperties(promotionRequest, promotion, ignoredProperties);

        return promotionRepository.save(promotion);
    }

    public void deletePromotion(Long id) {
        Promotion promotion = promotionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Promotion not found"));
        promotionRepository.delete(promotion);
    }

    private PromotionResponse convertToPromotionResponse(Promotion promotion) {
        PromotionResponse response = new PromotionResponse();
        BeanUtils.copyProperties(promotion, response);
        return response;
    }

    private Promotion convertPromotionRequestDTO(PromotionRequest promotionRequest) {
        return Promotion.builder()
                .codePromotion(promotionRequest.getCodePromotion())
                .name(promotionRequest.getName())
                .note(promotionRequest.getNote())
                .value(promotionRequest.getValue())
                .type(promotionRequest.getType())
                .startAt(promotionRequest.getStartAt())
                .endAt(promotionRequest.getEndAt())
                .build();
    }
}
