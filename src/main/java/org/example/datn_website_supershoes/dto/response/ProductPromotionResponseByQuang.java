package org.example.datn_website_supershoes.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.util.Date;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class ProductPromotionResponseByQuang {

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

    private Long idBrand;

    private Long idCategory;


}
