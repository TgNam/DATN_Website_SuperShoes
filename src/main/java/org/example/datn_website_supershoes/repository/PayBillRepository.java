package org.example.datn_website_supershoes.repository;

import org.example.datn_website_supershoes.dto.response.PayBillOrderResponse;
import org.example.datn_website_supershoes.dto.response.PayBillResponse;
import org.example.datn_website_supershoes.model.Bill;
import org.example.datn_website_supershoes.model.PayBill;
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
public interface PayBillRepository extends JpaRepository<PayBill, Long> {

    @Query("SELECT new org.example.datn_website_supershoes.dto.response.PayBillResponse(" +
            "pb.id,pb.tradingCode, pb.amount, pb.status, pb.createdAt, pb.type, pm.methodName, pb.note, a.name) " +
            "FROM PayBill pb " +
            "JOIN pb.bill b " +
            "JOIN pb.paymentMethod pm " +
            "JOIN b.employees a " +
            "WHERE b.codeBill = :codeBill " +
            "ORDER BY pb.createdAt DESC")
    List<PayBillResponse> listPayBillResponseByCodeBill(@Param("codeBill") String codeBill);


    Page<PayBill> findAll(Specification<PayBill> spec, Pageable pageable);

    @Query("""
            SELECT new org.example.datn_website_supershoes.dto.response.PayBillOrderResponse(
            pb.id,pb.tradingCode,pb.amount,pb.type,
            pb.status,pm.id,pm.methodName,pm.type,
            pm.status,b.id,b.codeBill,b.totalMerchandise,b.priceDiscount,b.totalAmount)
            FROM PayBill pb 
            INNER JOIN pb.bill b 
            INNER JOIN pb.paymentMethod pm WHERE b.codeBill=:codeBill And pb.status = 'COMPLETED'
            """)
    List<PayBillOrderResponse> findByCodeBill(@Param("codeBill") String codeBill);

    List<PayBill> findByBillCodeBill(@Param("codeBill") String codeBill);

    Optional<PayBill> findByBill(Bill bill);
}
