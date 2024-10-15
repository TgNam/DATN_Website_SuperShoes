package org.example.datn_website_supershoes.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
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

    @NotNull
    private Long idBill;

    @NotNull
    private Long idProductDetail;

    @NotNull
    @Min(1)
    private Integer quantity;

    private BigDecimal priceDiscount;

    private String note;

    private String status;
}
