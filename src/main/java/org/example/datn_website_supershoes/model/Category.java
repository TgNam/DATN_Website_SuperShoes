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
@Table(name = "Category")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category extends BaseEntity {

    @Column
    private String name;

    @JsonIgnore
    @JsonManagedReference(value = "categoryProductReference")
    @OneToMany(mappedBy = "category")
    private List<Product> products;

}
