package org.example.datn_website_supershoes.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressRequest {

    @NotNull(message = "id của tài khoản không được để trống")
    private Long idAccount;

    @NotBlank(message = "Tên là bắt buộc")
    @Size(min = 2, max = 50, message = "Tên phải chứa ít nhất 2 ký tự không được vượt quá 50 ký tự")
    @Pattern(regexp = "^[A-Za-zÀ-ỹ\\s]+$", message = "Tên không được chứa ký tự đặc biệt hoặc số")
    private String name;

    @NotBlank(message = "Số điện thoại là bắt buộc")
    @Pattern(regexp = "^0[0-9]{9,10}$", message = "Số điện thoại phải bắt đầu bằng số 0 và có từ 10 đến 11 số")
    private String phoneNumber;

    @NotBlank(message = "Địa chỉ là bắt buộc")
    @Size(min = 2, max = 100, message = "Tên phải chứa ít nhất 2 ký tự không được vượt quá 100 ký tự")
    private String address;

}
