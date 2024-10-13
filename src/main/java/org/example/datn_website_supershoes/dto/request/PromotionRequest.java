package org.example.datn_website_supershoes.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PromotionRequest {

    @NotBlank(message = "Tên chương trình khuyến mãi là bắt buộc")
    @Size(min = 2, max = 50, message = "Tên phải chứa ít nhất 2 ký tự và không được vượt quá 50 ký tự")
    @Pattern(regexp = "^[A-Za-zÀ-ỹ0-9\\s]+$", message = "Tên không được chứa ký tự đặc biệt")
    private String name;

    @NotNull(message = "Giá trị khuyến mãi là bắt buộc")
    @Positive(message = "Giá trị phải lớn hơn 0")
    @Max(value = 99, message = "Giá trị phải nhỏ hơn 100")
    private Double value;

    private Integer type;

    @Size(max = 255, message = "Ghi chú không được vượt quá 255 ký tự")
    private String note;

    @NotNull(message = "Ngày bắt đầu là bắt buộc")
    @Future(message = "Ngày bắt đầu phải sau thời điểm hiện tại")
    private Date startAt;

    @NotNull(message = "Ngày kết thúc là bắt buộc")
    @Future(message = "Ngày kết thúc không được trước thời điểm hiện tại")
    private Date endAt;

    public void validateEndDates() {
        if (startAt != null && endAt != null) {
            if (endAt.before(startAt)) {
                throw new IllegalArgumentException("Ngày kết thúc phải sau ngày bắt đầu");
            }
        }
    }
}
