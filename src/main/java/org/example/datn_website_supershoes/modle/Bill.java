package org.example.datn_website_supershoes.modle;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Bill")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Bill extends BaseEntity{
    @Column
    private String nameCustomer;
    @Column
    private String phoneNumber;
    @Column
    private String address;
    @Column
    private String note;
    @Column
    private Integer type;
    @Temporal(TemporalType.TIMESTAMP)
    @Column
    private Date deliveryDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column
    private Date receiveDate;
    @Column
    private BigDecimal totalMerchandise;
    @Column
    private BigDecimal moneyShip;
    @Column
    private BigDecimal priceDiscount;
    @Column
    private BigDecimal totalAmount;
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "id_voucher", referencedColumnName = "id")
    private Voucher voucher;
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "id_customer", referencedColumnName = "id")
    private Account customer;
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "id_employees", referencedColumnName = "id")
    private Account employees;
    @OneToOne
    private Guest guest;
    @JsonManagedReference
    @OneToMany(mappedBy = "bill")
    private List<PayBill> payBills;
    @JsonManagedReference
    @OneToMany(mappedBy = "bill")
    private List<BillHistory> billHistories;
    @JsonManagedReference
    @OneToMany(mappedBy = "bill")
    private List<BillDetail> billDetails;
}
