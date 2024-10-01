package org.example.datn_website_supershoes.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.example.datn_website_supershoes.model.ProductImage;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class BillDetailResponse {

    private Long id;

    private String productCode;

    private byte[] imageByte;

    private String nameProduct;

    private String nameColor;

    private Integer quantity;

    private BigDecimal totalAmount;

    private String status;


}
