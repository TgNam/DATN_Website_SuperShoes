package org.example.datn_website_supershoes.modle;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "ProductDetail")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product extends BaseEntity{
    @Column
    private String name;
    @Column
    private String productCode;
    @Lob
    @Column(name = "image",columnDefinition = "LONGBLOB")
    private byte[] imageByte;
    @Column
    private boolean gender;
    @JsonManagedReference
    @OneToMany(mappedBy = "product")
    private List<ProductDetail> productDetails;
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "id_brand", referencedColumnName = "id")
    private Brand brand;
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "id_category", referencedColumnName = "id")
    private Category category;
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "id_material", referencedColumnName = "id")
    private Material material;
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "id_shoe_sole", referencedColumnName = "id")
    private ShoeSole shoeSole;
    @JsonManagedReference
    @OneToMany(mappedBy = "product")
    private List<ProductFavorite> productFavorites;
}