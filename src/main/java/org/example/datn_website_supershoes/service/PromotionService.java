package org.example.datn_website_supershoes.service;

import org.example.datn_website_supershoes.Enum.Status;
import org.example.datn_website_supershoes.dto.response.PromotionResponse;
import org.example.datn_website_supershoes.model.Promotion;
import org.example.datn_website_supershoes.repository.PromotionRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PromotionService {

    @Autowired
    private PromotionRepository promotionRepository;


    public Promotion createPromotion(Promotion promotion) {
        return promotionRepository.save(promotion);
    }

    public List<PromotionResponse> getAllPromotions() {
        return promotionRepository.listPromotionResponseByStatus();
    }

    public Optional<Promotion> getPromotionById(Long id) {
        return promotionRepository.findById(id);
    }

    public Promotion updatePromotion(Long id, Promotion promotion) {
        Promotion existingPromotion = promotionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Promotion not found"));

        String[] ignoredProperties = {"id", "createdAt", "createdBy"};
        BeanUtils.copyProperties(promotion, existingPromotion, ignoredProperties);

        if (promotion.getCodePromotion() != null) {
            existingPromotion.setCodePromotion(promotion.getCodePromotion());
        }
        if (promotion.getName() != null) {
            existingPromotion.setName(promotion.getName());
        }
        if (promotion.getValue() != null) {
            existingPromotion.setValue(promotion.getValue());
        }
        if (promotion.getType() != null) {
            existingPromotion.setType(promotion.getType());
        }
        if (promotion.getNote() != null) {
            existingPromotion.setNote(promotion.getNote());
        }
        if (promotion.getStartAt() != null) {
            existingPromotion.setStartAt(promotion.getStartAt());
        }
        if (promotion.getEndAt() != null) {
            existingPromotion.setEndAt(promotion.getEndAt());
        }

        return promotionRepository.save(existingPromotion);
    }

    public void deletePromotion(Long id) {
        promotionRepository.deleteById(id);
    }
}
