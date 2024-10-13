package org.example.datn_website_supershoes.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString

public class PayBillResponse {

    private Long id;

    private String tradingCode;

    private BigDecimal amount;

    private String status;

    private Date createdAt;

    private Integer type;

    private String namePayment;

    private String note;

    private String nameEployee;
}
