package org.example.datn_website_supershoes.repository;

import org.example.datn_website_supershoes.dto.response.BillDetailOrderResponse;
import org.example.datn_website_supershoes.model.BillDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BillDetailByEmployeeRepository extends JpaRepository<BillDetail, Long> {
    @Query(value = "SELECT " +
            "new org.example.datn_website_supershoes.dto.response.BillDetailOrderResponse( " +
            "b.id,bd.id,bd.priceDiscount, bd.quantity, p.id, p.name, c.id, c.name, s.id, s.name, pd.id, pd.quantity, pd.price, " +
            "pro.id, pro.codePromotion,pro.value , pro.endAt, prod.id , prod.quantity ) " +
            "FROM BillDetail bd " +
            "INNER JOIN bd.bill b  " +
            "INNER JOIN bd.productDetail pd  " +
            "INNER JOIN pd.product p  " +
            "INNER JOIN pd.color c  " +
            "INNER JOIN pd.size s  " +
            "LEFT JOIN pd.promotionDetail prod ON prod.status = 'ONGOING' " +
            "LEFT JOIN prod.promotion pro " +
            "WHERE b.codeBill = :codeBill")
    List<BillDetailOrderResponse> getBillDetailsByCodeBill(String codeBill);

}
