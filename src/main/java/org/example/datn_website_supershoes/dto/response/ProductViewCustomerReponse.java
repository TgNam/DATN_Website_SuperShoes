package org.example.datn_website_supershoes.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class ProductViewCustomerReponse {

    private Long idProduct;

    private String nameProduct;

    private BigDecimal minPrice;

    private BigDecimal maxPrice;

    private BigDecimal minPriceAfterDiscount;

    private BigDecimal maxPriceAfterDiscount;


}
