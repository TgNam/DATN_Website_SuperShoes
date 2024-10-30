package org.example.datn_website_supershoes.dto.response;

import lombok.*;
import org.example.datn_website_supershoes.model.Promotion;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PromotionDetailResponse {
     private Promotion promotion;
     private List<ProductPromotionResponse> productPromotionResponses;
}
