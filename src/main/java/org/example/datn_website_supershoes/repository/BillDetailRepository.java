package org.example.datn_website_supershoes.repository;

import org.example.datn_website_supershoes.dto.response.BillDetailResponse;
import org.example.datn_website_supershoes.model.BillDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BillDetailRepository extends JpaRepository<BillDetail, Long> {

    @Query(value = """

                SELECT new org.example.datn_website_supershoes.dto.response.BillDetailResponse(bd.id,bd.quantity,bd.price_discount,bd.note,b.id,pd.id,b.status)
                FROM BillDetail bd JOIN Bill b ON bd.bill.id = b.id
                JOIN ProductDetail pd ON bd.productDetail.id = pd.id
                WHERE bd.status = :status

            """)
    List<BillDetailResponse> listBillResponeByStatus(@Param("status") String status);


}
