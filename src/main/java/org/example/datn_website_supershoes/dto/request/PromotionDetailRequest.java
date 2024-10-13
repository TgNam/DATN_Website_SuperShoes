package org.example.datn_website_supershoes.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PromotionDetailRequest {
    @NotNull(message = "Id của sản phẩm chi tiết là bắt buộc")
    private Long idProductDetail;
    @NotNull(message = "Số lượng của sản phẩm chi tiết là bắt buộc")
    private Integer quantity;
}
