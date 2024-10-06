package org.example.datn_website_supershoes.dto.response;


import jakarta.persistence.Column;
import lombok.*;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class AccountResponse {

    private Long id;

    private String name;

    private String email;

    private String phoneNumber;

    private String role;

    private Integer gender;

    private Date birthday;

    private Integer rewards;

    private String status;
}
