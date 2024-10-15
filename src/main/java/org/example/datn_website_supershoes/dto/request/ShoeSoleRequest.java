package org.example.datn_website_supershoes.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ShoeSoleRequest {
    @NotBlank(message = "Tên là bắt buộc")
    @Size(min = 2, max = 50, message = "Tên phải chứa ít nhất 2 ký tự và không được vượt quá 50 ký tự")
    @Pattern(regexp = "^[A-Za-zÀ-ỹ0-9\\s]+$", message = "Tên không được chứa ký tự đặc biệt")
    private String name;

}
