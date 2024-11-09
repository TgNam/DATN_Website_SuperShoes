package org.example.datn_website_supershoes.dto.request;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ProductImageRequest {

    private Long id;

    private List<byte[]> imageBytes;

    private Long idProductDetail;
}
