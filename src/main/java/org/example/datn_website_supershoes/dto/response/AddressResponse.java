package org.example.datn_website_supershoes.dto.response;

import jakarta.persistence.Column;
import lombok.*;

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
    private Integer type;
    private String status;
}
