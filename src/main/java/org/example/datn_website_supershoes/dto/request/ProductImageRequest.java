package org.example.datn_website_supershoes.dto.request;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ProductImageRequest {
    private byte[] imageByte;
}
