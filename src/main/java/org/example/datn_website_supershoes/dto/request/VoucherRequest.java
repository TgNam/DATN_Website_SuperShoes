package org.example.datn_website_supershoes.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
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

    @NotBlank(message = "Tên là bắt buộc")
    @Size(max = 255, message = "Tên không được vượt quá 255 ký tự")
    @Pattern(regexp = "^[A-Za-zÀ-ỹ0-9\\s]+$", message = "Tên không được chứa ký tự đặc biệt")
    private String name;

    private String note;

    @NotNull(message = "Giá trị giảm là bắt buộc")
    @DecimalMin(value = "0.01", inclusive = true, message = "Giá trị giảm phải lớn hơn 0")
    @DecimalMax(value = "10000000", inclusive = true, message = "Giá trị giảm không được vượt quá 10,000,000")
    private Double value;

    @NotNull(message = "Số lượng là bắt buộc")
    @Min(value = 1, message = "Số lượng phải là số nguyên dương")
    @Max(value = 1000, message = "Số lượng không được vượt quá 1,000")
    private Integer quantity;

    @DecimalMin(value = "0.01", inclusive = false, message = "Giảm giá tối đa phải lớn hơn 0")
    @DecimalMax(value = "10000000", inclusive = true, message = "Giảm giá tối đa không được vượt quá 10,000,000")
    private BigDecimal maximumDiscount;

    @NotNull(message = "Kiểu giảm giá là bắt buộc")
    @Min(value = 0, message = "Giá trị kiểu phải là 0 hoặc 1")
    @Max(value = 1, message = "Giá trị kiểu phải là 0 hoặc 1")
    private Integer type;

    @DecimalMin(value = "0.01", inclusive = false, message = "Giá trị đơn hàng tối thiểu phải lớn hơn 0")
    @DecimalMax(value = "10000000", inclusive = true, message = "Giá trị đơn hàng tối thiểu không được vượt quá 10,000,000")
    private BigDecimal minBillValue;

    @NotNull(message = "Ngày bắt đầu là bắt buộc")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date startAt;

    @NotNull(message = "Ngày kết thúc là bắt buộc")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date endAt;

    private Boolean isPrivate;

    private String status;

    private List<Long> accountIds;
}
