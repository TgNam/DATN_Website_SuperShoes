package org.example.datn_website_supershoes.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "PromotionDetail")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PromotionDetail extends BaseEntity {

    @Column
    private BigDecimal promotionPrice;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "id_productDetail", referencedColumnName = "id")
    private ProductDetail productDetail;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "id_promotion", referencedColumnName = "id")
    private Promotion promotion;
}