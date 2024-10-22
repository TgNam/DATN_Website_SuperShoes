package org.example.datn_website_supershoes.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BillRequest {

    private Long id;

    private String codeBill;

    private String nameCustomer;

    private String phoneNumber;

    private String address;

    private String note;

    private Integer type;

    private Date deliveryDate;

    private Date receiveDate;

    private BigDecimal totalMerchandise;

    private BigDecimal priceDiscount;

    private BigDecimal totalAmount;

    private Long idVoucher;

    private Long idCustomer;

    private Long idEmployees;

    private String status;
}
