package org.example.datn_website_supershoes.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;
@Entity
@Table(name = "Size")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Size extends BaseEntity {

    @Column
    private String name;

    @JsonIgnore
    @JsonManagedReference(value = "sizeProductDetailReference")
    @OneToMany(mappedBy = "size")
    private List<ProductDetail> productDetail;

}
