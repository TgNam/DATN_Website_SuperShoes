package org.example.datn_website_supershoes.dto.response;

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
public class PromotionResponse {

    private Long id;

    private String codePromotion;

    private String name;

    private Double value;

    private Integer type;

    private String note;

    private Date startAt;

    private Date endAt;

    private String status;
}
