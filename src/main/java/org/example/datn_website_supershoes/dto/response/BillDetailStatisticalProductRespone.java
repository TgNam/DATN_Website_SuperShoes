package org.example.datn_website_supershoes.dto.response;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder


public class BillDetailStatisticalProductRespone {

    private byte[] imageByte;

    private String nameProduct;

    private Integer quantity;

    private BigDecimal priceDiscount;
}
