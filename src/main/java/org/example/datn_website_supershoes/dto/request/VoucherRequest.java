package org.example.datn_website_supershoes.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VoucherRequest {

    private Long id;

    private String codeVoucher;

    @NotBlank(message = "Tên là bắt buộc")
    @Size(max = 255, message = "Tên không được vượt quá 255 ký tự")
    @Pattern(regexp = "^[A-Za-zÀ-ỹ0-9\\s]+$", message = "Tên không được chứa ký tự đặc biệt")
    private String name;

    private String note;

    @NotNull(message = "Giá trị giảm là bắt buộc")
    @DecimalMin(value = "1", inclusive = true, message = "Giá trị giảm phải lớn hơn 0")
    @DecimalMax(value = "2000000", inclusive = true, message = "Giá trị giảm không được vượt quá 2,000,000")
    private Double value;

    @NotNull(message = "Số lượng là bắt buộc")
    @Min(value = 1, message = "Số lượng phải là số nguyên dương")
    @Max(value = 1000, message = "Số lượng không được vượt quá 1,000")
    private Integer quantity;

    @DecimalMin(value = "1000", inclusive = false, message = "Giảm giá tối đa phải lớn hơn 1000")
    @DecimalMax(value = "2000000", inclusive = true, message = "Giảm giá tối đa không được vượt quá 2,000,000")
    private BigDecimal maximumDiscount;

    private Integer type;

    @DecimalMin(value = "1000", inclusive = false, message = "Giá trị đơn hàng tối thiểu phải lớn hơn 1000")
    @DecimalMax(value = "10000000", inclusive = true, message = "Giá trị đơn hàng tối thiểu không được vượt quá 10,000,000")
    private BigDecimal minBillValue;

    private Date startAt;

    private Date endAt;

    private Boolean isPrivate;

    private String status;

    private List<Long> accountIds;
}
