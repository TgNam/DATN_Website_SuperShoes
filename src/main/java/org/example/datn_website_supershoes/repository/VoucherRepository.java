package org.example.datn_website_supershoes.repository;

import org.example.datn_website_supershoes.dto.response.VoucherBillResponse;
import org.example.datn_website_supershoes.model.Voucher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface VoucherRepository extends JpaRepository<Voucher, Long>, JpaSpecificationExecutor<Voucher> {

    Page<Voucher> findAll(Specification<Voucher> spec, Pageable pageable);

    @Query(value = """
            SELECT new org.example.datn_website_supershoes.dto.response.VoucherBillResponse(
            v.id, v.codeVoucher, v.name, 
            v.note, v.value, v.quantity, 
            v.maximumDiscount, v.type, 
            v.minBillValue, v.startAt, 
            v.endAt, v.isPrivate, v.status
            )
            FROM Voucher v 
            WHERE v.status = :status AND v.isPrivate = :isPrivate
            """)
    List<VoucherBillResponse> findListVoucherByStatusAndIsPublic(@Param("status") String status,@Param("isPrivate") Boolean isPrivate);

    @Query(value = """
            SELECT new org.example.datn_website_supershoes.dto.response.VoucherBillResponse(
            v.id, v.codeVoucher, v.name, 
            v.note, v.value, v.quantity, 
            v.maximumDiscount, v.type, 
            v.minBillValue, v.startAt, 
            v.endAt, v.isPrivate, v.status
            )
            FROM Voucher v 
            WHERE v.status = :status AND v.id IN(:id) AND v.isPrivate = :isPrivate
            """)
    List<VoucherBillResponse> findListVoucherByListIdAndStatusAndPrivate(@Param("status") String status,@Param("id") List<Long> id,@Param("isPrivate") Boolean isPrivate);

    @Query(value = """
            SELECT new org.example.datn_website_supershoes.dto.response.VoucherBillResponse(
            v.id, v.codeVoucher, v.name, 
            v.note, v.value, v.quantity, 
            v.maximumDiscount, v.type, 
            v.minBillValue, v.startAt, 
            v.endAt, v.isPrivate, v.status
            )
            FROM Voucher v 
            WHERE v.status = :status AND v.id =:id
            """)
    Optional<VoucherBillResponse> findVoucherByListIdAndStatus(@Param("status") String status, @Param("id") Long id);


    Optional<Voucher> findByCodeVoucher(String codeVoucher);
}