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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Service
public class PromotionService {

    @Autowired
    private PromotionRepository promotionRepository;
    @Autowired
    private PromotionDetailService promotionDetailService;
    @Autowired
    private RandomPasswordGeneratorService randomCodePromotion;
    //chuyển trạng thái từ sắp diễn ra thành diễn ra
    @Transactional
    public void updateUpcomingDiscounts() {
        LocalDateTime now = LocalDateTime.now();
        //tìm kiếm trạng thái đang diễn ra
        List<Promotion> expiredDiscounts = promotionRepository.findUpcomingDiscounts(now,Status.UPCOMING.toString());
        for (Promotion discount : expiredDiscounts) {
            //cập nhật trạng thái
            discount.setStatus(Status.ONGOING.toString());
            //cập nhật trạng thái cho promotionDetail
            promotionDetailService.updatePromotionDetailUpcoming(discount.getId());
        }
        promotionRepository.saveAll(expiredDiscounts);
    }
    //chuyển các trạng thái sắp diễn ra, đang diễn ra, kết thúc sớm thành kết thúc
    @Transactional
    public void updateFinishedDiscounts() {
        LocalDateTime now = LocalDateTime.now();
        //tìm kiếm trạng thái đang sắp diễn ra, đang diễn ra, kết thúc sớm
        List<String> status = new ArrayList<>(Arrays.asList(Status.ONGOING.toString(),Status.UPCOMING.toString(),Status.ENDING_SOON.toString()));
        List<Promotion> expiredDiscounts = promotionRepository.findFinishedDiscounts(now,status);
        for (Promotion discount : expiredDiscounts) {
            //cập nhật trạng thái
            discount.setStatus(Status.FINISHED.toString());
            //cập nhật trạng thái cho promotionDetail
            promotionDetailService.updatePromotionDetailFinished(discount.getId());
        }
        promotionRepository.saveAll(expiredDiscounts);
    }

    public List<PromotionResponse> getAllPromotion() {
        return promotionRepository.listPromotionResponse();
    }

    public Promotion createPromotion(PromotionCreationRequest promotionCreationRequest) {
        promotionCreationRequest.getPromotionRequest().validateEndDates();
        Promotion promotion = promotionRepository.save(convertPromotionRequestDTO(promotionCreationRequest.getPromotionRequest()));
        if (promotionCreationRequest.getPromotionDetailRequest() != null && !promotionCreationRequest.getPromotionDetailRequest().isEmpty()) {
            promotionDetailService.createPromotionDetail(promotion, promotionCreationRequest.getPromotionDetailRequest());
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
