package org.example.datn_website_supershoes.modle;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "Guest")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Guest extends BaseEntity{
    @Column
    private String name;
    @Column
    private String phoneNumber;
    @Column
    private String address;
    @OneToOne(mappedBy = "guest")
    private Bill bill;
}
