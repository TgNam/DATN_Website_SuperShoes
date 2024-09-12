package org.example.datn_website_supershoes.repository;

import org.example.datn_website_supershoes.dto.response.AccountVoucherResponse;
import org.example.datn_website_supershoes.model.AccountVoucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AccountVoucherRepository extends JpaRepository<AccountVoucher, Long> {

    @Query("""
            SELECT new org.example.datn_website_supershoes.dto.response.AccountVoucherResponse(
            av.id, av.dateOfUse, a.id, a.name, v.id, v.name, av.status)
            FROM AccountVoucher av JOIN av.account a JOIN av.voucher v
            WHERE av.status = :status
            """)
    List<AccountVoucherResponse> listAccountVoucherResponsesByStatus(@Param("status") String status);
}
