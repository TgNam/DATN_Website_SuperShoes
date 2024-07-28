package org.example.datn_website_supershoes.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Voucher")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Voucher extends BaseEntity {

    @Column
    private String codeVoucher;

    @Column
    private String name;

    @Column
    private String note;

    @Column
    private Double value;

    @Column
    private Integer quantity;

    @Column
    private BigDecimal maximumDiscount;

    @Column
    private Integer type;

    @Column
    private BigDecimal minBillValue;

    @Temporal(TemporalType.TIMESTAMP)
    @Column
    private Date startAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column
    private Date endAt;

    @JsonManagedReference(value = "accountVoucherReference")
    @OneToMany(mappedBy = "voucher")
    private List<AccountVoucher> accountVouchers;

    @JsonManagedReference(value = "billReference")
    @OneToMany(mappedBy = "voucher")
    private List<Bill> bill;
}
