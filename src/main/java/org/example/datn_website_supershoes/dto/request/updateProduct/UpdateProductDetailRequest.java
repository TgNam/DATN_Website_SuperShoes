package org.example.datn_website_supershoes.dto.request.updateProduct;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
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
    @NotNull(message = "ID chi tiết sản phẩm không được để trống")
    private Long id;

    @NotNull(message = "Số lượng không được để trống")
    @Min(value = 0, message = "Số lượng phải lớn hơn hoặc bằng 0")
    private Integer quantity;

    @NotNull(message = "Giá không được để trống")
    @DecimalMin(value = "0.0", inclusive = false, message = "Giá phải lớn hơn 0")
    private BigDecimal price;

    @NotNull(message = "Màu sắc không được để trống")
    private Long idColor;

    @NotNull(message = "Kích cỡ không được để trống")
    private Long idSize;

    private List<byte[]> listImage;
}
