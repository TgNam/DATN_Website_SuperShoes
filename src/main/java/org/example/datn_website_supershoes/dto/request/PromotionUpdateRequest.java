package org.example.datn_website_supershoes.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PromotionUpdateRequest {
    @NotNull(message = "Id đợt giảm giá không được để trống")
    private Long id;

    @NotNull(message = "Ngày bắt đầu là bắt buộc")
    private Date startAt;

    @NotNull(message = "Ngày kết thúc là bắt buộc")
    @Future(message = "Ngày kết thúc không được trước thời điểm hiện tại")
    private Date endAt;

    private String status;
    public void validateEndDates() {
        if (startAt != null && endAt != null) {
            if (endAt.before(startAt)) {
                throw new IllegalArgumentException("Ngày kết thúc phải sau ngày bắt đầu");
            }
        }
    }
    public void updatePromotionStatus() {
        Date now = new Date();

        if (startAt != null) {
            if (startAt.before(now)) {
                this.status = "ONGOING";
            } else if (startAt.after(now)) {
                this.status = "UPCOMING";
            }
        }
    }
}
