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
public class ProductDetailResponse {

    private Long id;

    private int quantity;

    private BigDecimal price;

    private Long idProduct;

    private String nameProduct;

    private Long idSize;

    private String nameSize;

    private Long idColor;

    private String nameColor;

    private String status;
}
