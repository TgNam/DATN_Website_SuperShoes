package org.example.datn_website_supershoes.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "Color")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Color extends BaseEntity {

    @Column
    private String name;

    @Column
    private String codeColor;

    @JsonIgnore
    @JsonManagedReference(value = "colorProductDetailReference")
    @OneToMany(mappedBy = "color")
    private List<ProductDetail> productDetail;

}
