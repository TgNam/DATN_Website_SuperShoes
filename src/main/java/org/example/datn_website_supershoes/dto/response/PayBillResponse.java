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

public class PayBillResponse {

    private Long id;

    private BigDecimal amount;

    private String tradingCode;

    private String note;

    private Long idBill;

    private Long idPaymentMethod;

    private String status;
}
