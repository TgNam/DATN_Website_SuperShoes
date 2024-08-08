package org.example.datn_website_supershoes.dto.response;

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
public class AccountVoucherResponse {

    private Long id;

    private Date dateOfUse;

    private Long idAccount;

    private String nameAccount;

    private Long idVoucher;

    private String nameVoucher;

    private String status;

}
