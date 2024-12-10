package org.example.datn_website_supershoes.repository;


import org.example.datn_website_supershoes.dto.response.BillDetailResponse;
import org.example.datn_website_supershoes.dto.response.BillDetailStatisticalProductRespone;
import org.example.datn_website_supershoes.model.BillDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
@Repository
public interface BillDetailRepository extends JpaRepository<BillDetail, Long> {

    Page<BillDetail> findAll(Specification<BillDetail> spec, Pageable pageable);

    @Query("SELECT bd FROM BillDetail bd WHERE bd.id = :id")
    Optional<BillDetail> findById(@Param("id") Long id);

    @Query("SELECT bd FROM BillDetail bd WHERE bd.bill.id = :idBill AND bd.productDetail.id = :idProductDetail AND bd.priceDiscount=:priceDiscount")
    Optional<BillDetail> findByIdBillAndIdProductDetailAndPriceDiscount(@Param("idBill") Long idBill, @Param("idProductDetail") Long idProductDetail,@Param("priceDiscount") BigDecimal priceDiscount);

    @Query("SELECT bd FROM BillDetail bd WHERE bd.bill.id = :idBill")
    List<BillDetail> findByIdBill(@Param("idBill") Long idBill);

    @Query("SELECT p.imageByte, p.id, p.name, bd.quantity, bd.priceDiscount,( bd.quantity* bd.priceDiscount) " +
            "FROM BillDetail bd " +
            "JOIN bd.bill b " +
            "JOIN bd.productDetail pd " +
            "JOIN pd.product p " +
            "WHERE b.status = 'COMPLETED'")
    List<Object[]> findProductDetailsForBillDetails();

    @Query("""
        SELECT COALESCE(SUM(bd.quantity), 0) 
        FROM BillDetail bd 
        WHERE bd.bill.id = :idBill 
        AND bd.productDetail.id = :idProductDetail
        """)
    Integer sumQuantityBillDetailByIdBillAdnIdProductDetail(@Param("idBill") Long idBill, @Param("idProductDetail") Long idProductDetail);

}