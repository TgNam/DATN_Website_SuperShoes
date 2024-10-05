package org.example.datn_website_supershoes.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressResponse {
    private Long id;
    private String name;
    private String phoneNumber;
    private String address;
    private Long idAccount;
    private Integer type;
    private String status;
}
