package org.example.datn_website_supershoes.dto.request.updateProduct;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProductRequest {

    private Long id;

    private String name;

    private byte[] image;

    private boolean gender;

    private Long idBrand;

    private Long idCategory;

    private Long idMaterial;

    private Long idShoeSole;

    private List<UpdateProductDetailRequest> productDetailRequest;
}
