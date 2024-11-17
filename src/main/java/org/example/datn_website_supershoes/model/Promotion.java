package org.example.datn_website_supershoes.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
@Builder
public class Promotion extends BaseEntity {

    @Column
    private String codePromotion;

    @Column
    private String name;

    @Column
    private Double value;

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

    @JsonIgnore
    @OneToMany(mappedBy = "promotion")
    private List<PromotionDetail> promotionDetail;

}
