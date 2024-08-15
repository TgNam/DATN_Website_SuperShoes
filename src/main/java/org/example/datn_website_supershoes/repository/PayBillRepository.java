package org.example.datn_website_supershoes.repository;

import org.example.datn_website_supershoes.dto.response.PayBillResponse;
import org.example.datn_website_supershoes.model.PayBill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PayBillRepository extends JpaRepository<PayBill, Long> {

    @Query(value = """
                SELECT new org.example.datn_website_supershoes.dto.response.PayBillResponse(pb.id,pb.amount,pb.tradingCode,pb.note,b.id,pm.id,b.status)
                FROM PayBill pb
                JOIN Bill b ON pb.bill.id = b.id
                JOIN PaymentMethod pm ON pb.paymentMethod.id = pm.id
                WHERE pb.status = :status
            """)
    List<PayBillResponse> listPayBillResponeByStatus(@Param("status") String status);


}
