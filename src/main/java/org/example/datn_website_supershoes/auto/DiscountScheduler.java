package org.example.datn_website_supershoes.auto;

import org.example.datn_website_supershoes.service.PromotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class DiscountScheduler {

    @Autowired
    PromotionService promotionService;
    @Scheduled(fixedRate = 3600000)
    public void checkAndUpdateExpiredDiscounts() {
        promotionService.updateExpiredDiscounts();
    }
}
