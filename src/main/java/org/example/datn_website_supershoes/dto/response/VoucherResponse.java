package org.example.datn_website_supershoes.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
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
public class VoucherResponse {

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

    private Date createdAt;

    private String createdBy;

    private Date updatedAt;

    private String updatedBy;

    private List<Long> accountIds;
}
