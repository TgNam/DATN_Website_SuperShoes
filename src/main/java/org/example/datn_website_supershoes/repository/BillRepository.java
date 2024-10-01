package org.example.datn_website_supershoes.repository;


import org.example.datn_website_supershoes.dto.response.BillResponse;
import org.example.datn_website_supershoes.dto.response.BillSummaryResponse;
import org.example.datn_website_supershoes.model.Bill;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BillRepository extends JpaRepository<Bill, Long> {

    @Query("SELECT new org.example.datn_website_supershoes.dto.response.BillResponse(" +
            "b.id, b.codeBill, c.name, b.phoneNumber, b.address, b.note, b.type, b.deliveryDate, b.receiveDate, " +
            "b.totalMerchandise, b.priceDiscount, b.totalAmount, b.guest.id, v.id, v.name, c.id, e.id, e.name, b.status, b.createdAt) " +
            "FROM Bill b " +
            "JOIN b.customer c " +
            "LEFT JOIN b.voucher v " +
            "LEFT JOIN b.employees e " +
            "WHERE b.status = 'ACTIVE' " +
            "ORDER BY b.createdAt ASC ")
    List<BillResponse> listBillResponseByStatus();

    Page<Bill> findAll(Specification<Bill> spec, Pageable pageable);

    @Query("SELECT new org.example.datn_website_supershoes.dto.response.BillSummaryResponse(" +
            "b.status, b.nameCustomer, b.address, b.phoneNumber, b.note) " +
            "FROM Bill b " +
            "WHERE b.codeBill = :codeBill")
    Optional<BillSummaryResponse> findBillSummaryByCodeBill(@Param("codeBill") String codeBill);

    @Query("SELECT b FROM Bill b WHERE b.codeBill = :codeBill")
    Optional<Bill> findByCodeBill(@Param("codeBill") String codeBill);

}
