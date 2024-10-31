package org.example.datn_website_supershoes.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDetailPromoRequest {
    @NotNull(message = "Id của sản phẩm chi tiết là bắt buộc")
    private Long idProductDetail;
    @NotNull(message = "Số lượng của sản phẩm chi tiết là bắt buộc")
    @Positive(message = "Số lượng phải lớn hơn 0")
    private Integer quantity;
}
