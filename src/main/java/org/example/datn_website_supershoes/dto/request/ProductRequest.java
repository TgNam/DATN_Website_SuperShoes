package org.example.datn_website_supershoes.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest {

    private String name;

    private byte[] image;

    private boolean gender;

    private Long idBrand;

    private Long idCategory;

    private Long idMaterial;

    private Long idShoeSole;

    private List<ProductDetailRequest> productDetailRequest;

}
