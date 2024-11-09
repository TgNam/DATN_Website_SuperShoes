package org.example.datn_website_supershoes.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "PayBill")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PayBill extends BaseEntity {

    @Column
    private BigDecimal amount;

    @Column
    private String tradingCode;

    @Column
    private Integer type;

    @Column
    private String note;

    @JsonBackReference(value = "billPayBillReference")
    @ManyToOne
    @JoinColumn(name = "id_bill", referencedColumnName = "id")
    private Bill bill;

    @JsonBackReference(value = "paymentMethodPayBillReference")
    @ManyToOne
    @JoinColumn(name = "id_payment_method", referencedColumnName = "id")
    private PaymentMethod paymentMethod;



}
