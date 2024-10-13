package org.example.datn_website_supershoes.dto.request;

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
public class BillDetailRequest {

    private Long id;

    private Integer quantity;

    private BigDecimal priceDiscount;

    private String note;

    private Long idBill;

    private Long idProductDetail;

    private String status;
}
