package org.example.datn_website_supershoes.service;

import org.example.datn_website_supershoes.Enum.Status;
import org.example.datn_website_supershoes.dto.request.PromotionDetailRequest;
import org.example.datn_website_supershoes.dto.request.PromotionRequest;
import org.example.datn_website_supershoes.model.ProductDetail;
import org.example.datn_website_supershoes.model.Promotion;
import org.example.datn_website_supershoes.model.PromotionDetail;
import org.example.datn_website_supershoes.repository.ProductDetailRepository;
import org.example.datn_website_supershoes.repository.PromotionDetailRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class PromotionDetailService {

    @Autowired
    private PromotionDetailRepository promotionDetailRepository;
    @Autowired
    private ProductDetailRepository productDetailRepository;
    public List<PromotionDetail> createPromotionDetail(Promotion promotion,List<PromotionDetailRequest> promotionDetailRequest) {
        for (PromotionDetailRequest request : promotionDetailRequest){
            Optional<PromotionDetail> promotionDetailOptional = promotionDetailRepository.findPromotionDetailByIdProductDetailAndStatuses(
                    request.getIdProductDetail(),
                    Arrays.asList(Status.ONGOING.toString(), Status.UPCOMING.toString(), Status.ENDING_SOON.toString()));
            if (promotionDetailOptional.isPresent()){
                PromotionDetail promotionDetail = promotionDetailOptional.get();
                promotionDetail.setStatus(Status.FINISHED.toString());
                promotionDetailRepository.save(promotionDetail);
            }
        }
        List<PromotionDetail> promotionDetails = promotionDetailRepository.saveAll(convertPromotionDetailRequestDTO(promotion,promotionDetailRequest));
        return promotionDetails;
    }


    private List<PromotionDetail> convertPromotionDetailRequestDTO(Promotion promotion,List<PromotionDetailRequest> promotionDetailRequest) {
        List<PromotionDetail> promotionDetails = new ArrayList<>();
        for (PromotionDetailRequest request : promotionDetailRequest){
            Optional<ProductDetail> productDetail = productDetailRepository.findById(request.getIdProductDetail());
            if (!productDetail.isPresent()){
                throw new RuntimeException("Id "+productDetail.get().getId()+" của sản phẩm chi tiết không tồn tại trên hệ thống.");
            }
            // Tính toán giá khuyến mãi
            BigDecimal promotionPrice = productDetail.get().getPrice().multiply(BigDecimal.valueOf(1 - promotion.getValue() / 100));
            // Tạo đối tượng PromotionDetail
            PromotionDetail promotionDetail = new PromotionDetail();
            promotionDetail.setQuantity(request.getQuantity());
            promotionDetail.setPromotionPrice(promotionPrice);
            promotionDetail.setProductDetail(productDetail.get());
            promotionDetail.setPromotion(promotion);
            promotionDetail.setStatus(Status.UPCOMING.toString());
            promotionDetails.add(promotionDetail);
        }
        return promotionDetails;
    }
}
