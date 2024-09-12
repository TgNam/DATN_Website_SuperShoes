package org.example.datn_website_supershoes.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest {

    private Long id;

    private String name;

    private String productCode;

    private byte[] imageByte;

    private boolean gender;

    private Long idBrand;

    private Long idCategory;

    private Long idMaterial;

    private Long idShoeSole;

}
