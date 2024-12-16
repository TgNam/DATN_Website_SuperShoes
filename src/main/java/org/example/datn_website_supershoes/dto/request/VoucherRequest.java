package org.example.datn_website_supershoes.dto.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @NotBlank(message = "Tên phiếu giảm giá là bắt buộc")
    @Size(max = 255, message = "Tên phiếu giảm giá không được vượt quá 255 ký tự")
    @Pattern(regexp = "^[A-Za-zÀ-ỹ0-9\\s]+$", message = "Tên phiếu giảm giá không được chứa ký tự đặc biệt")
    private String name;

    private String note;

    @NotNull(message = "Giá trị là bắt buộc")
    @DecimalMin(value = "1", inclusive = true, message = "Giá trị tối thiểu 1%")
    @DecimalMax(value = "100", inclusive = false, message = "Giá trị tối đa 99%")
    private Double value;

    @NotNull(message = "Số lượng là bắt buộc")
    @Min(value = 1, message = "Số lượng tối thiểu là 1")
    @Max(value = 1000, message = "Số lượng tối đa là 1,000")
    private Integer quantity;

    @DecimalMin(value = "1000", inclusive = false, message = "Giảm giá tối đa phải lớn hơn 1,000 VND")
    @DecimalMax(value = "2000000", inclusive = true, message = "Giảm giá tối đa không được vượt quá 2,000,000 VND")
    private BigDecimal maximumDiscount;

    private Integer type;

    @DecimalMin(value = "1000", inclusive = false, message = "Giá trị đơn hàng tối thiểu phải lớn hơn 1,000 VND")
    @DecimalMax(value = "10000000", inclusive = true, message = "Giá trị đơn hàng tối thiểu không được vượt quá 10,000,000 VND")
    private BigDecimal minBillValue;

    @NotNull(message = "Ngày bắt đầu là bắt buộc")
    @Future(message = "Ngày bắt đầu phải sau thời điểm hiện tại")
    private Date startAt;

    @NotNull(message = "Ngày kết thúc là bắt buộc")
    @Future(message = "Ngày kết thúc phải sau thời điểm hiện tại")
    private Date endAt;

    private Boolean isPrivate;

    private String status;

    private List<Long> accountIds;
}
