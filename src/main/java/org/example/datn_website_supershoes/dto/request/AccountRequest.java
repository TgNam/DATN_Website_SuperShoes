package org.example.datn_website_supershoes.dto.request;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountRequest {

    private Long id;

    private String name;

    private String email;

    private String password;

    private String role;

    private String status;

}
