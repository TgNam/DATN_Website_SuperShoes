package org.example.datn_website_supershoes.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "ProductFavorite")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductFavorite extends BaseEntity {

    @JsonBackReference(value = "accountProductFavoriteReference")
    @ManyToOne
    @JoinColumn(name = "id_account", referencedColumnName = "id")
    private Account account;

    @JsonBackReference(value = "productProductFavoriteReference")
    @ManyToOne
    @JoinColumn(name = "id_product", referencedColumnName = "id")
    private Product product;

}
