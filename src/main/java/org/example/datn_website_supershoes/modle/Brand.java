package org.example.datn_website_supershoes.modle;

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
@Table(name = "Brand")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Brand extends  BaseEntity{
    @Column
    private String name;
    @JsonManagedReference
    @OneToMany(mappedBy = "brand")
    private List<Product> products;
}