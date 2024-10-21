package org.example.datn_website_supershoes.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Address")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Address extends BaseEntity {

    @Column
    private String codeCity;

    @Column
    private String codeDistrict;

    @Column
    private String codeWard;

    @Column
    private String address;

    @Column
    private Integer type;
    @JsonBackReference(value = "accountAddressReference")
    @ManyToOne
    @JoinColumn(name = "id_account", referencedColumnName = "id")
    private Account account;
}
