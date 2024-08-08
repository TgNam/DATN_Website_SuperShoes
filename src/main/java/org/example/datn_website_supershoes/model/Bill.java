package org.example.datn_website_supershoes.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
public class Bill extends BaseEntity {

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

    @OneToOne
    @JoinColumn(name = "guest_id")
    private Guest guest;

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
    @OneToMany(mappedBy = "bill")
    private List<PayBill> payBills;

    @JsonIgnore
    @JsonManagedReference(value = "billBillHistoryReference")
    @OneToMany(mappedBy = "bill")
    private List<BillHistory> billHistories;

    @JsonIgnore
    @JsonManagedReference(value = "billBillDetailReference")
    @OneToMany(mappedBy = "bill")
    private List<BillDetail> billDetails;
}
