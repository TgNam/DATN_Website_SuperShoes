package org.example.datn_website_supershoes.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonInclude;
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

    @JsonIgnore
    @JsonManagedReference(value = "accountAddressReference")
    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Address> address;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @OneToOne(mappedBy = "account")
    private Cart cart;

    @JsonIgnore
    @JsonManagedReference(value = "accountProductFavoriteReference")
    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductFavorite> productFavorites;

    @JsonIgnore
    @JsonManagedReference(value = "customerBillReference")
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Bill> customerBills;

    @JsonIgnore
    @JsonManagedReference(value = "employeeBillReference")
    @OneToMany(mappedBy = "employees", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Bill> employeeBills;

    @JsonIgnore
    @JsonManagedReference(value = "accountBillHistoryReference")
    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BillHistory> billHistories;

}
