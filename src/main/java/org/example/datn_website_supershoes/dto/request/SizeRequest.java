package org.example.datn_website_supershoes.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SizeRequest {

    @NotBlank(message = "Tên kích cỡ là bắt buộc")
    @Pattern(regexp = "\\d+", message = "Tên kích cỡ phải là số")
    @Min(value = 32, message = "Kích cỡ phải lớn hơn hoặc bằng 32")
    @Max(value = 46, message = "Kích cỡ phải nhỏ hơn hoặc bằng 46")
    private String name;
}
