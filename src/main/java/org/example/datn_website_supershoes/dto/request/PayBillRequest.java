package org.example.datn_website_supershoes.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class PayBillRequest {
    @NotNull(message = "Giá tiền là bắt buộc")
    private BigDecimal amount;
    @NotBlank(message = "Mã hóa đơn là bắt buộc")
    private String codeBill;
    @NotNull(message = "Phương thức thanh toán là bắt buộc")
    private Integer type;
}
