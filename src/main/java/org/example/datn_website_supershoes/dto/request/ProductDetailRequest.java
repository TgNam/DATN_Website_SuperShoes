package org.example.datn_website_supershoes.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDetailRequest {

    private Long id;

    private int quantity;

    private BigDecimal price;

    private Long idProduct;

    private Long idSize;

    private Long idColor;

}
