package org.example.datn_website_supershoes.repository;

import org.example.datn_website_supershoes.dto.response.BillResponse;
import org.example.datn_website_supershoes.model.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BillRepository extends JpaRepository<Bill, Long> {

    @Query("""
            SELECT new org.example.datn_website_supershoes.dto.response.BillResponse(
            b.id, b.nameCustomer, b.phoneNumber, b.address, b.note, b.type, b.deliveryDate, 
            b.receiveDate, b.totalMerchandise, b.priceDiscount, b.totalAmount, b.guest, 
            b.voucher.id, b.voucher.name, b.customer.id, b.employees.id, b.employees.name, b.status)
            FROM Bill b WHERE b.status = :status
            """)
    List<BillResponse> listBillResponseByStatus(@Param("status") String status);

}
