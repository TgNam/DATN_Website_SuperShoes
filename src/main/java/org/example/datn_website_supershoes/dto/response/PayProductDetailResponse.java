package org.example.datn_website_supershoes.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;
@Getter
@Setter
@NoArgsConstructor
public class PayProductDetailResponse {
    private Long idProduct;

    private String nameProduct;

    private Long idColor;

    private String nameColor;

    private Long idSize;

    private String nameSize;

    private Long idProductDetail;

    private Integer quantityProductDetail;

    private BigDecimal productDetailPrice;

    private Long idPromotion;

    private String codePromotion;

    private Double value;

    private Date endAtByPromotion;

    private Long idPromotionDetail;

    private Integer quantityPromotionDetail;

    private Integer quantityBuy;

    private String error;

    public PayProductDetailResponse(Long idProduct, String nameProduct, Long idColor, String nameColor, Long idSize, String nameSize, Long idProductDetail, Integer quantityProductDetail, BigDecimal productDetailPrice, Long idPromotion, String codePromotion, Double value, Date endAtByPromotion, Long idPromotionDetail, Integer quantityPromotionDetail) {
        this.idProduct = idProduct;
        this.nameProduct = nameProduct;
        this.idColor = idColor;
        this.nameColor = nameColor;
        this.idSize = idSize;
        this.nameSize = nameSize;
        this.idProductDetail = idProductDetail;
        this.quantityProductDetail = quantityProductDetail;
        this.productDetailPrice = productDetailPrice;
        this.idPromotion = idPromotion;
        this.codePromotion = codePromotion;
        this.value = value;
        this.endAtByPromotion = endAtByPromotion;
        this.idPromotionDetail = idPromotionDetail;
        this.quantityPromotionDetail = quantityPromotionDetail;
    }

    public PayProductDetailResponse(Long idProductDetail, Integer quantityBuy, String error) {
        this.idProductDetail = idProductDetail;
        this.quantityBuy = quantityBuy;
        this.error = error;
    }
    public BigDecimal calculatePricePerProductDetail() {
        BigDecimal price = productDetailPrice;
        int quantity = quantityBuy;

        if (value == null) {
            // Không có khuyến mãi
            return price.multiply(BigDecimal.valueOf(quantity));
        } else if (quantity <= quantityPromotionDetail) {
            // Có khuyến mãi và số lượng trong giỏ <= số lượng áp dụng khuyến mãi
            BigDecimal discountPrice = price.multiply(BigDecimal.valueOf(1 - value / 100));
            return discountPrice.multiply(BigDecimal.valueOf(quantity));
        } else {
            // Có khuyến mãi và số lượng trong giỏ > số lượng áp dụng khuyến mãi
            BigDecimal discountPrice = price.multiply(BigDecimal.valueOf(1 - value / 100))
                    .multiply(BigDecimal.valueOf(quantityPromotionDetail));
            BigDecimal nonDiscountPrice = price.multiply(BigDecimal.valueOf(quantity - quantityPromotionDetail));
            return discountPrice.add(nonDiscountPrice);
        }
    }
}
