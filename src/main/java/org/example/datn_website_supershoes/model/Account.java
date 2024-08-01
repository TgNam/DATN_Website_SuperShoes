package org.example.datn_website_supershoes.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "Account")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account extends BaseEntity {

    @Column
    private String name;

    @Column
    private String email;

    @Column
    private String password;

    @Column
    private String role;

    @Column
    private Integer rewards;

    @JsonManagedReference(value = "accountAddressReference")
    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Address> address;

    @OneToOne(mappedBy = "account")
    private Cart cart;

    @JsonManagedReference(value = "accountProductFavoriteReference")
    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductFavorite> productFavorites;

    @JsonManagedReference(value = "customerBillReference")
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Bill> customerBills;

    @JsonManagedReference(value = "employeeBillReference")
    @OneToMany(mappedBy = "employees", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Bill> employeeBills;

    @JsonManagedReference(value = "accountBillHistoryReference")
    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BillHistory> billHistories;

}
