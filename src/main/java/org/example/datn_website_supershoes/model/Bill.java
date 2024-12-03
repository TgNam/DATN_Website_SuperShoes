package org.example.datn_website_supershoes.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Bill")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Bill extends BaseEntity {

    @Column
    private String codeBill;

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
    private BigDecimal priceDiscount;

    @Column
    private BigDecimal totalAmount;

    @JsonBackReference(value = "billReference")
    @ManyToOne
    @JoinColumn(name = "voucher_id")
    private Voucher voucher;

    @JsonBackReference(value = "customerBillReference")
    @ManyToOne
    @JoinColumn(name = "id_customer", referencedColumnName = "id")
    private Account customer;

    @JsonBackReference(value = "employeeBillReference")
    @ManyToOne
    @JoinColumn(name = "id_employees", referencedColumnName = "id")
    private Account employees;

    @JsonIgnore
    @JsonManagedReference(value = "billPayBillReference")
    @OneToMany(mappedBy = "bill", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PayBill> payBills;

    @JsonIgnore
    @JsonManagedReference(value = "billBillHistoryReference")
    @OneToMany(mappedBy = "bill", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BillHistory> billHistories;

    @JsonIgnore
    @JsonManagedReference(value = "billBillDetailReference")
    @OneToMany(mappedBy = "bill", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BillDetail> billDetails;
}
