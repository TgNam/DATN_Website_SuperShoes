package org.example.datn_website_supershoes.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SizeRequest {

    @NotNull(message = "Tên kích cỡ là bắt buộc")
    @Pattern(regexp = "\\d+", message = "Tên kích cỡ phải là số")
    @Min(value = 35, message = "Kích cỡ phải lớn hơn hoặc bằng 35")
    @Max(value = 50, message = "Kích cỡ phải nhỏ hơn hoặc bằng 50")
    private String name;
}
