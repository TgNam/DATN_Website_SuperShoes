package org.example.datn_website_supershoes.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.datn_website_supershoes.model.ProductDetail;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartDetailRequest {

    private String codeCart;

    private Integer quantity;

    private Long idCart;

    private ProductDetail idProductDetail;

    private String status;
}
