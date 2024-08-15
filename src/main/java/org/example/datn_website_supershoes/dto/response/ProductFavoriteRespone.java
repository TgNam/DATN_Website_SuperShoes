package org.example.datn_website_supershoes.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductFavoriteRespone {

    private Long id;

    private Long idAccount;

    private String nameAccount;

    private Long idProduct;

    private String nameProduct;

    private String status;

}
