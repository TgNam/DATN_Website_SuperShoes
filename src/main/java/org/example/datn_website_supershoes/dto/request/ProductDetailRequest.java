package org.example.datn_website_supershoes.dto;

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
public class ProductDetailRequest {

    private Integer quantity;
    private BigDecimal price;
    private String description;

    private Long productId;  // Chỉ gửi ID của Product
    private Long sizeId;     // Chỉ gửi ID của Size
    private Long colorId;    // Chỉ gửi ID của Color

    private Long imageId;    // Chỉ gửi ID của Color
    private byte[] imageByte;  // Danh sách hình ảnh Base64
}
