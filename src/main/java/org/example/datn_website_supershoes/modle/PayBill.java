package org.example.datn_website_supershoes.modle;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "PayBill")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PayBill extends BaseEntity{
    @Column
    private BigDecimal amount;
    @Column
    private String tradingCode;
    @Column
    private String note;
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "id_bill", referencedColumnName = "id")
    private Bill bill;
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "id_payment_method", referencedColumnName = "id")
    private PaymentMethod paymentMethod;
}
