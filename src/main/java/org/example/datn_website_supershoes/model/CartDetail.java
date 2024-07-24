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

@Entity
@Table(name = "CartDetail")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartDetail extends BaseEntity {

    @Column
    private String codeCart;

    @Column
    private Integer quantity;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "id_cart", referencedColumnName = "id")
    private Cart cart;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "id_productDetail", referencedColumnName = "id")
    private ProductDetail productDetail;
}
