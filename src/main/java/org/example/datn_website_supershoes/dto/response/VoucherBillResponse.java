package org.example.datn_website_supershoes.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VoucherBillResponse {
    private Long id;

    private String codeVoucher;

    private String name;

    private String note;

    private Double value;

    private Integer quantity;

    private BigDecimal maximumDiscount;

    private Integer type;

    private BigDecimal minBillValue;

    private Date startAt;

    private Date endAt;

    private Boolean isPrivate;

    private String status;
}
