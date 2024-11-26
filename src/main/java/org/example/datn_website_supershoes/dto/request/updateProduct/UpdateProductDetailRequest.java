package org.example.datn_website_supershoes.dto.request.updateProduct;

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
public class UpdateProductDetailRequest {

    private Long id;

    private Integer quantity;

    private BigDecimal price;

    private Long idColor;

    private Long idSize;

    private List<byte[]> listImage;
}
