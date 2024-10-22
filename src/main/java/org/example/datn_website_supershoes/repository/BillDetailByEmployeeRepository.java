package org.example.datn_website_supershoes.repository;

import org.example.datn_website_supershoes.dto.response.BillDetailOrderResponse;
import org.example.datn_website_supershoes.model.BillDetail;
import org.example.datn_website_supershoes.model.ProductDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BillDetailByEmployeeRepository extends JpaRepository<BillDetail, Long> {
    @Query(value = "SELECT " +
            "new org.example.datn_website_supershoes.dto.response.BillDetailOrderResponse( " +
            "b.id,bd.id, p.id, p.name, c.id, c.name, s.id, s.name, pd.id, bd.quantity, pd.price, " +
            "pro.id, pro.codePromotion , pro.endAt, prod.id , prod.promotionPrice ) " +
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
