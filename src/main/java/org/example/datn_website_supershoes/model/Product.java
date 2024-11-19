package org.example.datn_website_supershoes.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "Product")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product extends BaseEntity {

    @Column
    private String name;

    @Column
    private String productCode;

    @Lob
    @Column(name = "image", columnDefinition = "LONGBLOB")
    private byte[] imageByte;

    @Column
    private boolean gender;


    @OneToMany(mappedBy = "product")
    private List<ProductDetail> productDetails;

    @JsonBackReference(value = "brandProductReference")
    @ManyToOne
    @JoinColumn(name = "id_brand", referencedColumnName = "id")
    private Brand brand;

    @JsonBackReference(value = "categoryProductReference")
    @ManyToOne
    @JoinColumn(name = "id_category", referencedColumnName = "id")
    private Category category;

    @JsonBackReference(value = "materialProductReference")
    @ManyToOne
    @JoinColumn(name = "id_material", referencedColumnName = "id")
    private Material material;

    @JsonBackReference(value = "shoeSoleProductReference")
    @ManyToOne
    @JoinColumn(name = "id_shoe_sole", referencedColumnName = "id")
    private ShoeSole shoeSole;

    @JsonIgnore
    @JsonManagedReference(value = "productProductFavoriteReference")
    @OneToMany(mappedBy = "product")
    private List<ProductFavorite> productFavorites;

}
