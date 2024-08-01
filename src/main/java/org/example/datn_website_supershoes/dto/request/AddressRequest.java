package org.example.datn_website_supershoes.dto.request;

import jakarta.persistence.Column;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressRequest {

    private String name;
    private String phoneNumber;
    private String address;
}
