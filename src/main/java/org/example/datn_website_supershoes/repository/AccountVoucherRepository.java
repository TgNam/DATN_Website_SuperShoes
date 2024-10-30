package org.example.datn_website_supershoes.repository;

import jakarta.transaction.Transactional;
import org.example.datn_website_supershoes.dto.response.AccountVoucherResponse;
import org.example.datn_website_supershoes.model.AccountVoucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountVoucherRepository extends JpaRepository<AccountVoucher, Long> {

    @Query("""
            SELECT new org.example.datn_website_supershoes.dto.response.AccountVoucherResponse(
            av.id, av.dateOfUse, a.id, a.name, v.id, v.name, av.status)
            FROM AccountVoucher av 
            JOIN av.account a 
            JOIN av.voucher v
            WHERE av.status = :status
            """)
    List<AccountVoucherResponse> listAccountVoucherResponsesByStatus(@Param("status") String status);

    @Modifying
    @Transactional
    @Query("DELETE FROM AccountVoucher av WHERE av.voucher.id = :voucherId")
    void deleteByVoucherId(@Param("voucherId") Long voucherId);


    @Modifying
    @Transactional
    @Query("UPDATE AccountVoucher av SET av.status = :status WHERE av.voucher.id = :voucherId")
    void updateStatusByVoucherId(@Param("voucherId") Long voucherId, @Param("status") String status);

    @Query("SELECT av.account.id FROM AccountVoucher av WHERE av.voucher.id = :voucherId")
    List<Long> findAccountIdsByVoucherId(@Param("voucherId") Long voucherId);

    @Query("SELECT av.voucher.id FROM AccountVoucher av WHERE av.account.id = :idAccount")
    List<Long> findIdVoucherByIdAccount(@Param("idAccount") Long idAccount);
}
