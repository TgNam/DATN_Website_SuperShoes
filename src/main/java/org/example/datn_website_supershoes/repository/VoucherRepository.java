package org.example.datn_website_supershoes.repository;

import org.example.datn_website_supershoes.dto.response.VoucherResponse;
import org.example.datn_website_supershoes.model.Voucher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VoucherRepository extends JpaRepository<Voucher, Long> {

    @Query("""
            SELECT new org.example.datn_website_supershoes.dto.response.VoucherResponse(
            v.id, v.codeVoucher, v.name, v.note, v.value, v.quantity, v.maximumDiscount, v.type,
            v.minBillValue, v.startAt, v.endAt, v.status)
            FROM Voucher v WHERE v.status = :status
            """)
    List<VoucherResponse> listVoucherResponseByStatus(@Param("status") String status);

    Page<Voucher> findAll(Specification<Voucher> spec, Pageable pageable);
}
