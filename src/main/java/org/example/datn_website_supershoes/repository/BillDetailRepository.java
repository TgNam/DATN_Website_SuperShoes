package org.example.datn_website_supershoes.repository;


import org.example.datn_website_supershoes.dto.response.BillDetailResponse;
import org.example.datn_website_supershoes.model.BillDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface BillDetailRepository extends JpaRepository<BillDetail, Long> {

    @Query("SELECT new org.example.datn_website_supershoes.dto.response.BillDetailResponse(" +
            "bd.id, p.productCode,c.id, p.imageByte, p.name, c.name, bd.quantity, pd.price, bd.status, bd.priceDiscount) " +
            "FROM BillDetail bd " +
            "JOIN bd.productDetail pd " +
            "JOIN pd.product p " +
            "JOIN pd.color c " +
            "JOIN bd.bill b " +
            "WHERE b.codeBill = :codeBill " +
            "GROUP BY bd.id, p.productCode, p.imageByte, p.name, c.name, bd.quantity, pd.price, b.status " +
            "ORDER BY pd.createdAt DESC")
    List<BillDetailResponse> listBillDetailResponseByCodeBill(@Param("codeBill") String codeBill);

    // Pagination support for listing BillDetails based on specifications
    Page<BillDetail> findAll(Specification<BillDetail> spec, Pageable pageable);

    @Query("SELECT bd FROM BillDetail bd WHERE bd.id = :id")
    Optional<BillDetail> findById(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query(value = "DELETE bd " +
            "FROM bill_detail bd " +
            "JOIN product_detail pd ON pd.id = bd.id_product_detail " +
            "JOIN product p ON p.id = pd.id_product " +
            "JOIN color c ON c.id = pd.id_color " +
            "WHERE p.product_code = :productCode AND c.name = :nameColor", nativeQuery = true)
    void deleteByProductCodeAndColorId(@Param("productCode") String productCode, @Param("nameColor") String nameColor);

    @Query("SELECT bd FROM BillDetail bd WHERE bd.bill.id = :idBill AND bd.productDetail.id = :idProductDetail")
    Optional<BillDetail> findByIdBillAndIdProductDetail(@Param("idBill") Long idBill, @Param("idProductDetail") Long idProductDetail);

    @Query("SELECT bd FROM BillDetail bd WHERE bd.productDetail.product.productCode = :productCode AND bd.productDetail.color.name = :nameColor")
    List<BillDetail> findByProductCodeAndColorName(@Param("productCode") String productCode, @Param("nameColor") String nameColor);
}