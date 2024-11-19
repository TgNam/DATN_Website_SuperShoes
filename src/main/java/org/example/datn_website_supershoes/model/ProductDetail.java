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

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "ProductDetail")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDetail extends BaseEntity {

    @Column
    private Long id;

    @Column
    private Integer quantity;

    @Column
    private BigDecimal price;

    @Column(name = "description")
    private String description;


    @ManyToOne
    @JoinColumn(name = "id_product", referencedColumnName = "id")
    @JsonIgnore
    private Product product;

    @ManyToOne
    @JoinColumn(name = "id_size", referencedColumnName = "id")
    private Size size;

    @ManyToOne
    @JoinColumn(name = "id_color", referencedColumnName = "id")
    private Color color;

    @JsonManagedReference(value = "productDetailProductImageReference")
    @OneToMany(mappedBy = "productDetail")
    private List<ProductImage> productImage;

    @JsonManagedReference(value = "productDetailPromotionDetailReference")
    @OneToMany(mappedBy = "productDetail")
    @JsonIgnore
    private List<PromotionDetail> promotionDetail;

    @JsonManagedReference(value = "productDetailCartDetailReference")
    @OneToMany(mappedBy = "productDetail")
    @JsonIgnore
    private List<CartDetail> cartDetails;

    @JsonManagedReference(value = "productDetailBillDetailReference")
    @OneToMany(mappedBy = "productDetail")
    @JsonIgnore
    private List<BillDetail> billDetails;
}