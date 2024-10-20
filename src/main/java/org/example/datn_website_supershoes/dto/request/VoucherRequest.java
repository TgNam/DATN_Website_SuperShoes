package org.example.datn_website_supershoes.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VoucherRequest {

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

    private List<Long> accountIds;
}
