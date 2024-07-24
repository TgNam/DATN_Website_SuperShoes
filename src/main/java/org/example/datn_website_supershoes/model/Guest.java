package org.example.datn_website_supershoes.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Guest")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Guest extends BaseEntity {

    @Column
    private String name;

    @Column
    private String phoneNumber;

    @Column
    private String address;

    @OneToOne(mappedBy = "guest")
    private Bill bill;
}
