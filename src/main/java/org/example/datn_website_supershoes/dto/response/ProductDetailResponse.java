package org.example.datn_website_supershoes.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDetailResponse {

    private Long id;

    private int quantity;

    private BigDecimal price;

    private String description;

    private Long idProduct;

    private String nameProduct;

    private String productCode;

    private Long idSize;

    private String nameSize;

    private Long idColor;

    private String nameColor;

    private List<byte[]> imageBytes;

    private boolean gender;

    private Long idBrand;

    private String nameBrand;

    private Long idCategory;

    private String nameCategory;

    private Long idMaterial;

    private String nameMaterial;

    private Long idShoeSole;

    private String nameShoeSole;


    private String status;
}
