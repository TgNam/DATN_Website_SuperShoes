package org.example.datn_website_supershoes.dto.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class AccountResponse {

    private Long id;

    private String name;

    private String email;

    private String password;

    private String role;

    private String gmail;

    private String number;

    private String status;
}
