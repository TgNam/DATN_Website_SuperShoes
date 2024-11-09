package org.example.datn_website_supershoes.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "PaymentMethod")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentMethod extends BaseEntity {

    @Column
    private String methodName;

    @Column
    private String note;

    @Column
    private Integer type;

    @JsonIgnore
    @JsonManagedReference(value = "paymentMethodPayBillReference")
    @OneToMany(mappedBy = "paymentMethod")
    private List<PayBill> payBills;

}
