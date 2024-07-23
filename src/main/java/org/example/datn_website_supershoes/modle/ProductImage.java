package org.example.datn_website_supershoes.modle;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
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
public class ProductImage extends  BaseEntity{
    @Lob
    @Column(name = "image",columnDefinition = "LONGBLOB")
    private byte[] imageByte;
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "id_productImage", referencedColumnName = "id")
    private ProductDetail productDetail;
}
