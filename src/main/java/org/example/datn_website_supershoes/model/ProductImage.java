package org.example.datn_website_supershoes.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "ProductImage")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductImage extends BaseEntity {

    @Lob
    @Column(name = "image", columnDefinition = "LONGBLOB")
    private byte[] imageByte;

    @JsonBackReference(value = "productDetailProductImageReference")
    @ManyToOne
    @JoinColumn(name = "id_productImage", referencedColumnName = "id")
    private ProductDetail productDetail;

}
