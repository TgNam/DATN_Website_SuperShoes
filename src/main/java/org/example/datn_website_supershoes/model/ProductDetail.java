package org.example.datn_website_supershoes.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
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
    private Integer quantity;

    @Column
    private BigDecimal price;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "id_product", referencedColumnName = "id")
    private Product product;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "id_size", referencedColumnName = "id")
    private Size size;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "id_color", referencedColumnName = "id")
    private Color color;

    @JsonManagedReference
    @OneToMany(mappedBy = "productDetail")
    private List<ProductImage> productImage;

    @JsonManagedReference
    @OneToMany(mappedBy = "productDetail")
    private List<PromotionDetail> promotionDetail;

    @JsonManagedReference
    @OneToMany(mappedBy = "productDetail")
    private List<CartDetail> cartDetails;

    @JsonManagedReference
    @OneToMany(mappedBy = "productDetail")
    private List<BillDetail> billDetails;
}
