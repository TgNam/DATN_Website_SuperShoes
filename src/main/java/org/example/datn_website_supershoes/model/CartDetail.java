package org.example.datn_website_supershoes.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "CartDetail")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartDetail extends BaseEntity {

    @Column
    private String codeCart;

    @Column
    private int quantity;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "id_cart", referencedColumnName = "id")
    private Cart cart;


    @ManyToOne
    @JoinColumn(name = "product_detail_id")
    private ProductDetail productDetail;

}
