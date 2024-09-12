package org.example.datn_website_supershoes.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "Brand")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Brand extends BaseEntity {

    @Column
    private String name;

    @JsonIgnore
    @JsonManagedReference(value = "brandProductReference")
    @OneToMany(mappedBy = "brand")
    private List<Product> products;
}
