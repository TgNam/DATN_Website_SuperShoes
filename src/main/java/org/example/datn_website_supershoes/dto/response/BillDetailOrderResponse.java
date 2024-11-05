package org.example.datn_website_supershoes.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class BillDetailOrderResponse {
    private Long idBill;

    private Long idBillDetail;

    private BigDecimal priceDiscount;

    private Long idProduct;

    private String nameProduct;

    private Long idColor;

    private String nameColor;

    private Long idSize;

    private String nameSize;

    private Long idProductDetail;

    private Integer quantity;

    private BigDecimal productDetailPrice;

    private Long idPromotion;

    private String codePromotion;

    private Date endAtByPromotion;

    private Long idPromotionDetail;

    private BigDecimal promotionPrice;
}
