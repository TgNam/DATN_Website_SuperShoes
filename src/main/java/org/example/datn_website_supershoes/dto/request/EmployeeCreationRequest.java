package org.example.datn_website_supershoes.dto.request;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeCreationRequest {
    @Valid
    private AccountRequest accountRequest;
    @Valid
    private AddressCreationRequest addressRequest;
}
