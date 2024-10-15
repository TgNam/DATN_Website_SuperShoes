package org.example.datn_website_supershoes.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class BillDetailResponse {

    private Long id;

    private String productCode;

    private Long idColor;

    private byte[] imageByte;

    private String nameProduct;

    private String nameColor;

    private Integer quantity;

    private BigDecimal totalAmount;

    private String status;

    private BigDecimal priceDiscount;

}
