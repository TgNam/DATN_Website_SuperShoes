package org.example.datn_website_supershoes.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductFavoriteRequest {

    private Long id;

    private Long idAccount;

    private Long idProduct;

}
