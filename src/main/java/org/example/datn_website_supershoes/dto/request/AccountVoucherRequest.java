package org.example.datn_website_supershoes.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountVoucherRequest {

    private Long id;

    private Date dateOfUse;

    private List<Long> accountIds;

    private String status;

    private VoucherRequest voucherRequest;
}
