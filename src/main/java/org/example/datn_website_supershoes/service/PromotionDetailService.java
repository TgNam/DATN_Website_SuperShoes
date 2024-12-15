package org.example.datn_website_supershoes.service;

import org.example.datn_website_supershoes.Enum.Status;
import org.example.datn_website_supershoes.dto.request.ProductDetailPromoRequest;
import org.example.datn_website_supershoes.dto.request.PromotionDetailRequest;
import org.example.datn_website_supershoes.dto.response.ProductPromotionResponse;
import org.example.datn_website_supershoes.dto.response.ProductPromotionResponseByQuang;
import org.example.datn_website_supershoes.model.ProductDetail;
import org.example.datn_website_supershoes.model.Promotion;
import org.example.datn_website_supershoes.model.PromotionDetail;
import org.example.datn_website_supershoes.repository.ProductDetailRepository;
import org.example.datn_website_supershoes.repository.PromotionDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PromotionDetailService {

    @Autowired
    PromotionDetailRepository promotionDetailRepository;
    @Autowired
    ProductDetailRepository productDetailRepository;
    @Autowired
    ProductDetailService productDetailService;

    //cập nhật trạng thái từ sắp diễn ra thành diễn ra
    public List<ProductPromotionResponse> findProductPromotionResponseByIdPromotion(Long idPromotion) {
        return promotionDetailRepository.findProductByIdPromotion(idPromotion);
    }

    public List<ProductPromotionResponseByQuang> findProductPromotionResponseByIdPromotionByQuang(Long idPromotion) {
        return promotionDetailRepository.findProductByIdPromotionByQuang(idPromotion);
    }

    public List<ProductPromotionResponse> filterListProductPromotion(Long idPromotion, String search, String nameSize, String nameColor, String priceRange) {
        return promotionDetailRepository.findProductByIdPromotion(idPromotion).stream()
                .filter(ProductPromotionResponse -> ProductPromotionResponse.getNameProduct().toLowerCase().contains(search.trim().toLowerCase()))
                .filter(ProductPromotionResponse -> ProductPromotionResponse.getNameSize().toLowerCase().contains(nameSize.trim().toLowerCase()))
                .filter(ProductPromotionResponse -> ProductPromotionResponse.getNameColor().toLowerCase().contains(nameColor.trim().toLowerCase()))
                .filter(ProductPromotionResponse -> {
                    BigDecimal priceToFilter = ProductPromotionResponse.getProductDetailPrice();
                    return productDetailService.filterByPriceRange(priceToFilter, priceRange);
                })
                .collect(Collectors.toList());
    }

    public void updatePromotionDetailUpcoming(Long idPromotion) {
        List<PromotionDetail> promotionDetails = promotionDetailRepository.findPromotionDetailByIdPromotionAndStatuses(idPromotion, Arrays.asList(Status.UPCOMING.toString()));
        for (PromotionDetail pd : promotionDetails) {
            pd.setStatus(Status.ONGOING.toString());
            promotionDetailRepository.save(pd);
        }
    }

    //cập nhật trạng thái từ sắp diễn ra, đang diễn ra, kết thúc sớm thành kết thúc
    public void updatePromotionDetailFinished(Long idPromotion) {
        List<PromotionDetail> promotionDetails = promotionDetailRepository.findPromotionDetailByIdPromotionAndStatuses(idPromotion, Arrays.asList(Status.ONGOING.toString(), Status.UPCOMING.toString(), Status.ENDING_SOON.toString()));
        for (PromotionDetail pd : promotionDetails) {
            pd.setStatus(Status.FINISHED.toString());
            promotionDetailRepository.save(pd);
        }
    }

    public void updateStatusPromotionDetail(Long idPromotion, String status) {
        List<PromotionDetail> promotionDetails = promotionDetailRepository.findPromotionDetailByIdPromotionAndStatuses(idPromotion, Arrays.asList(Status.ONGOING.toString(), Status.UPCOMING.toString(), Status.ENDING_SOON.toString()));
        for (PromotionDetail pd : promotionDetails) {
            pd.setStatus(status);
            promotionDetailRepository.save(pd);
        }
    }

    public List<PromotionDetail> createPromotionDetail(Promotion promotion, List<ProductDetailPromoRequest> productDetailPromoRequest) {
        for (ProductDetailPromoRequest request : productDetailPromoRequest) {
            Optional<PromotionDetail> promotionDetailOptional = promotionDetailRepository.findPromotionDetailByIdProductDetailAndStatuses(
                    request.getIdProductDetail(),
                    Arrays.asList(Status.ONGOING.toString(), Status.UPCOMING.toString(), Status.ENDING_SOON.toString()));
            if (promotionDetailOptional.isPresent()) {
                PromotionDetail promotionDetail = promotionDetailOptional.get();
                promotionDetail.setStatus(Status.FINISHED.toString());
                promotionDetailRepository.save(promotionDetail);
            }
        }
        List<PromotionDetail> promotionDetails = promotionDetailRepository.saveAll(convertPromotionDetailRequestDTO(promotion, productDetailPromoRequest));
        return promotionDetails;
    }

    @Transactional
    public void updatePromotionDetail(Promotion promotion, List<PromotionDetailRequest> promotionDetailRequests) {
            for (PromotionDetailRequest request : promotionDetailRequests) {
                //Tìm kiếm đợt giảm giá chi tiết cần sửa bởi id
                PromotionDetail promotionDetail = promotionDetailRepository.findById(request.getIdPromotionDetail())
                        .orElseThrow(() -> new RuntimeException("Không tìm thấy tài nguyên giảm giá trong hệ thống!"));

                //Tìm kiếm đợt giảm giá chi tiết bởi id sản phẩm chi tiết và trạng thái
                Optional<PromotionDetail> promotionDetailOptional = promotionDetailRepository.findPromotionDetailByIdProductDetailAndStatuses(
                        promotionDetail.getProductDetail().getId(),
                        Arrays.asList(Status.ONGOING.toString(), Status.UPCOMING.toString(), Status.ENDING_SOON.toString()));
                //Nếu tìm thấy đợt giảm giá bởi id sản phẩm chi tiết và trạng thái thì trạng thái chuyển sang FINISHED
                if (promotionDetailOptional.isPresent()) {
                    promotionDetail.setStatus(Status.FINISHED.toString());
                    promotionDetailRepository.save(promotionDetail);
                }
                promotionDetail.setQuantity(request.getQuantity());
                if (request.getQuantity() == 0) {
                    promotionDetail.setStatus(Status.FINISHED.toString());
                } else {
                    promotionDetail.setStatus(promotion.getStatus());
                }
                promotionDetailRepository.save(promotionDetail);
            }
    }

    private List<PromotionDetail> convertPromotionDetailRequestDTO(Promotion promotion, List<ProductDetailPromoRequest> productDetailPromoRequest) {
        List<PromotionDetail> promotionDetails = new ArrayList<>();
        for (ProductDetailPromoRequest request : productDetailPromoRequest) {
            Optional<ProductDetail> productDetail = productDetailRepository.findById(request.getIdProductDetail());
            if (productDetail.isEmpty()) {
                throw new RuntimeException("Không tìm thấy tài nguyên sản phẩm trong hệ thống!");
            }
            // Tạo đối tượng PromotionDetail
            PromotionDetail promotionDetail = new PromotionDetail();
            promotionDetail.setQuantity(request.getQuantity());
            promotionDetail.setProductDetail(productDetail.get());
            promotionDetail.setPromotion(promotion);
            promotionDetail.setStatus(Status.UPCOMING.toString());
            promotionDetails.add(promotionDetail);
        }
        return promotionDetails;
    }

    public Map<Long, PromotionDetail> findPromotionDetailByIdProductDetail(List<Long> ids) {
        Map<Long, PromotionDetail> promotionDetailMap = new HashMap<>();

        for (Long id : ids) {
            PromotionDetail promotionDetail = promotionDetailRepository
                    .findPromotionDetailByIdProductDetailAndStatuses(id, List.of(Status.ONGOING.toString()))
                    .orElse(null);
            promotionDetailMap.put(id, promotionDetail);
        }
        return promotionDetailMap;
    }
}
