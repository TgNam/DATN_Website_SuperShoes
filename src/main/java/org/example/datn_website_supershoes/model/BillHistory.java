package org.example.datn_website_supershoes.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "BillHistory")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BillHistory extends BaseEntity {

    @Column
    private String note;

    @JsonBackReference(value = "billBillHistoryReference")
    @ManyToOne
    @JoinColumn(name = "bill_id")
    private Bill bill;

    @JsonBackReference(value = "accountBillHistoryReference")
    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    private String status;

}
