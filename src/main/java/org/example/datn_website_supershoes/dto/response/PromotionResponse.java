package org.example.datn_website_supershoes.dto.response;

import jakarta.persistence.Column;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PromotionResponse {

    private String codePromotion;


    private String name;


    private Double value;


    private Integer type;


    private String note;


    private Date startAt;

    private Date endAt;

    private String status;

}
