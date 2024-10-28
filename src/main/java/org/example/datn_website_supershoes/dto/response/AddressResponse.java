package org.example.datn_website_supershoes.dto.response;

import jakarta.persistence.Column;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressResponse {

    private Long id;

    private String codeCity;

    private String codeDistrict;

    private String codeWard;

    private String address;

    private Long idAccount;

    private String nameAccount;

    private String phoneNumber;

    private Integer gender;

    private Date birthday;

    private Integer type;

    private String status;

}
