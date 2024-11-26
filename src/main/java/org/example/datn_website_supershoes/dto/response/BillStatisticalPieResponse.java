package org.example.datn_website_supershoes.dto.response;


import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class BillStatisticalPieResponse {

    private Date createdAt;

    private int numberBill;

    private BigDecimal price;

    private String status;

}
