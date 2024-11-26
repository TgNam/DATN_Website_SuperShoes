package org.example.datn_website_supershoes.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class BillHistoryRequest {

    private String note;

    private Date createdAt;

    private Long billId;

    private Long accountId;

    private String status;

}
