package org.example.datn_website_supershoes.modle;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
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
public class BillHistory extends BaseEntity{
    @Column
    private String note;
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "id_bill", referencedColumnName = "id")
    private Bill bill;
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "id_account", referencedColumnName = "id")
    private Account account;
}
