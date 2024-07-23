package org.example.datn_website_supershoes.modle;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Promotion")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Promotion extends BaseEntity{
    @Column
    private String codePromotion;
    @Column
    private String name;
    @Column
    private double value;
    @Column
    private Integer type;
    @Column
    private String note;

    @Temporal(TemporalType.TIMESTAMP)
    @Column
    private Date startAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column
    private Date endAt;

    @JsonManagedReference
    @OneToMany(mappedBy = "promotion")
    private List<PromotionDetail> promotionDetail;
}
