package org.example.datn_website_supershoes.auto;

import org.example.datn_website_supershoes.model.CartDetail;
import org.example.datn_website_supershoes.service.BillByEmployeeService;
import org.example.datn_website_supershoes.service.CartDetailService;
import org.example.datn_website_supershoes.service.PromotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class DiscountScheduler {

    @Autowired
    PromotionService promotionService;
    @Autowired
    private BillByEmployeeService billByEmployeeService;
    @Autowired
    private CartDetailService cartDetailService;
    @Scheduled(fixedRate = 20000)
    public void checkAndUpdateExpiredDiscounts() {
        promotionService.updateUpcomingDiscounts();
        promotionService.updateFinishedDiscounts();
        billByEmployeeService.findBillsOlderThanOneDay();
        cartDetailService.findCartDetailsOlderThanOneDay();
    }
}
