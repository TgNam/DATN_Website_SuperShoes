package org.example.datn_website_supershoes.modle;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "AccountVoucher")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountVoucher extends BaseEntity{
    @Temporal(TemporalType.TIMESTAMP)
    @Column
    private Date dateOfUse;
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "id_account", referencedColumnName = "id")
    private Account account;
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "id_voucher", referencedColumnName = "id")
    private Voucher voucher;
}
