package org.example.datn_website_supershoes.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountVoucherRequest {

    private Long id;

    private Date dateOfUse;

    private Long idAccount;

    private Long idVoucher;

    private String status;
}
