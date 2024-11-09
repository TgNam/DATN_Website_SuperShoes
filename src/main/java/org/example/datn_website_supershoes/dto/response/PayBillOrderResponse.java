package org.example.datn_website_supershoes.dto.response;

import jakarta.persistence.Column;
import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PayBillOrderResponse {

    private Long idPayBill;

    private String tradingCode;

    private BigDecimal amount;

    private Integer typePayBill;

    private String statusPayBill;

    private Long idPaymentMethod;

    private String methodName;

    private Integer typePaymentMethod;

    private String statusPaymentMethod;

    private Long idBill;

    private String codeBill;

    private BigDecimal totalMerchandise;

    private BigDecimal priceDiscount;

    private BigDecimal totalAmount;

}
