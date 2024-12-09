package org.example.datn_website_supershoes.dto.request.updateProduct;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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

    @NotNull(message = "ID sản phẩm không được để trống")
    private Long id;

    @NotBlank(message = "Tên sản phẩm không được để trống")
    @Size(max = 255, message = "Tên sản phẩm không được vượt quá 255 ký tự")
    private String name;

    private byte[] image;

    private boolean gender;

    @NotNull(message = "Thương hiệu không được để trống")
    private Long idBrand;

    @NotNull(message = "Danh mục không được để trống")
    private Long idCategory;

    @NotNull(message = "Chất liệu không được để trống")
    private Long idMaterial;

    @NotNull(message = "Đế giày không được để trống")
    private Long idShoeSole;

//    @NotNull(message = "Danh sách chi tiết sản phẩm không được để trống")
//    @Size(min = 1, message = "Phải có ít nhất một chi tiết sản phẩm")
    private List<@Valid UpdateProductDetailRequest> productDetailRequest;
}
