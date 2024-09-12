package org.example.datn_website_supershoes.repository;

import org.example.datn_website_supershoes.dto.response.BillResponse;
import org.example.datn_website_supershoes.model.Bill;
import org.example.datn_website_supershoes.model.Voucher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BillRepository extends JpaRepository<Bill, Long> {

    @Query("SELECT new org.example.datn_website_supershoes.dto.response.BillResponse(" +
            "b.id, c.name, b.phoneNumber, b.address, b.note, b.type, b.deliveryDate, b.receiveDate, " +
            "b.totalMerchandise, b.priceDiscount, b.totalAmount, b.guest.id, v.id, v.name, c.id, e.id, e.name, b.status,b.createdAt) " +
            "FROM Bill b " +
            "JOIN b.customer c " +
            "LEFT JOIN b.voucher v " +
            "LEFT JOIN b.employees e " )

    List<BillResponse> listBillResponseByStatus();

    Page<Bill> findAll(Specification<Bill> spec, Pageable pageable);







}
