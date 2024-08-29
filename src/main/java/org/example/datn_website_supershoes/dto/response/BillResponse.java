package org.example.datn_website_supershoes.dto.response;

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
public class BillResponse {

    private Long id;

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

    private Long idGuest;

    private Long idVoucher;

    private String nameVoucher;

    private Long idCustomer;

    private Long idEmployees;

    private String nameEmployees;

    private String status;

    private Date createdAt;


}
