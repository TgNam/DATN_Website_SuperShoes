package org.example.datn_website_supershoes.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "Account")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
    @OneToMany(mappedBy = "account")
    private List<Address> address;

    @OneToOne(mappedBy = "account")
    private Cart cart;

    @JsonManagedReference(value = "accountProductFavoriteReference")
    @OneToMany(mappedBy = "account")
    private List<ProductFavorite> productFavorites;

    @JsonManagedReference(value = "customerBillReference")
    @OneToMany(mappedBy = "customer")
    private List<Bill> customerBills;

    @JsonManagedReference(value = "employeeBillReference")
    @OneToMany(mappedBy = "employees")
    private List<Bill> employeeBills;

    @JsonManagedReference(value = "accountBillHistoryReference")
    @OneToMany(mappedBy = "account")
    private List<BillHistory> billHistories;

}
