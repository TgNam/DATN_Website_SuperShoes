package org.example.datn_website_supershoes.model;

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
import com.fasterxml.jackson.annotation.JsonIgnore;
@Entity
@Table(name = "ShoeSole")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ShoeSole extends BaseEntity {

    @Column
    private String name;

    @JsonIgnore
    @JsonManagedReference(value = "shoeSoleProductReference")
    @OneToMany(mappedBy = "shoeSole")
    private List<Product> products;

}
