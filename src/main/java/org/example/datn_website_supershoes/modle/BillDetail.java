package org.example.datn_website_supershoes.modle;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "BillDetail")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BillDetail extends BaseEntity{
    @Column
    private Integer quantity;
    @Column
    private BigDecimal price_discount;
    @Column
    private String note;
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "id_bill", referencedColumnName = "id")
    private Bill bill;
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "id_product_detail", referencedColumnName = "id")
    private ProductDetail productDetail;
}
