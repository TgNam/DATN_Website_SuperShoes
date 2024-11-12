package org.example.datn_website_supershoes.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductResponse {

    private Long id;

    private String name;

    private String productCode;

    private byte[] imageByte;

    private boolean gender;

    private Long idBrand;

    private String nameBrand;

    private Long idCategory;

    private String nameCategory;

    private Long idMaterial;

    private String nameMaterial;

    private Long idShoeSole;

    private String nameShoeSole;

    private String status;

    private List<Long> idProductDetail;

    private int quantity;

    private BigDecimal price;

    private String description;

    private Long idSize;

    private String nameSize;

    private Long idColor;

    private String nameColor;

    private List<byte[]> imageBytes;


}
