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
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {


    Page<Bill> findAll(Specification<Bill> spec, Pageable pageable);

    @Query("SELECT new org.example.datn_website_supershoes.dto.response.BillSummaryResponse(" +
            "b.codeBill,b.status, b.nameCustomer, b.address, b.phoneNumber, b.note) " +
            "FROM Bill b " +
            "WHERE b.codeBill = :codeBill")
    Optional<BillSummaryResponse> findBillSummaryByCodeBill(@Param("codeBill") String codeBill);

    @Query("SELECT b FROM Bill b WHERE b.codeBill = :codeBill")
    Optional<Bill> findByCodeBill(@Param("codeBill") String codeBill);

}
