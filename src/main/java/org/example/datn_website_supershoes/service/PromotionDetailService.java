package org.example.datn_website_supershoes.service;

import org.example.datn_website_supershoes.model.PromotionDetail;
import org.example.datn_website_supershoes.repository.PromotionDetailRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PromotionDetailService {

    @Autowired
    private PromotionDetailRepository promotionDetailRepository;

    public PromotionDetail createPromotionDetail(PromotionDetail promotionDetail) {
        return promotionDetailRepository.save(promotionDetail);
    }

    public List<PromotionDetail> getAllPromotionDetails() {
        return promotionDetailRepository.findAll();
    }

    public Optional<PromotionDetail> getPromotionDetailById(Long id) {
        return promotionDetailRepository.findById(id);
    }

    public PromotionDetail updatePromotionDetail(Long id, PromotionDetail promotionDetail) {
        PromotionDetail existingPromotionDetail = promotionDetailRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("PromotionDetail not found"));

        String[] ignoredProperties = {"id", "createdAt", "createdBy"};
        BeanUtils.copyProperties(promotionDetail, existingPromotionDetail, ignoredProperties);

        if (promotionDetail.getPromotionPrice() != null) {
            existingPromotionDetail.setPromotionPrice(promotionDetail.getPromotionPrice());
        }
        if (promotionDetail.getProductDetail() != null) {
            existingPromotionDetail.setProductDetail(promotionDetail.getProductDetail());
        }
        if (promotionDetail.getPromotion() != null) {
            existingPromotionDetail.setPromotion(promotionDetail.getPromotion());
        }

        return promotionDetailRepository.save(existingPromotionDetail);
    }

    public void deletePromotionDetail(Long id) {
        promotionDetailRepository.deleteById(id);
    }
}
