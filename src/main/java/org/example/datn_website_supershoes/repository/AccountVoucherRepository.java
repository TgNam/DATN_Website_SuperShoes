package org.example.datn_website_supershoes.repository;

import org.example.datn_website_supershoes.dto.request.AccountVoucherRequest;
import org.example.datn_website_supershoes.model.AccountVoucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountVoucherRepository extends JpaRepository<AccountVoucher, Long> {

    @Query("""
            SELECT new org.example.datn_website_supershoes.dto.request.AccountVoucherRequest(av.dateOfUse, a.id, a.name, v.id, v.name)
            FROM AccountVoucher av JOIN av.account a JOIN av.voucher v 
            WHERE av.status = :status
            """)
    List<AccountVoucherRequest> findAccountVoucherRequestsByStatus(@Param("status") String status);

}
