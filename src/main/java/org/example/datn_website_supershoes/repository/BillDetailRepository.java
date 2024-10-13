package org.example.datn_website_supershoes.repository;

import org.example.datn_website_supershoes.dto.response.BillDetailResponse;
import org.example.datn_website_supershoes.model.BillDetail;
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
public interface BillDetailRepository extends JpaRepository<BillDetail, Long> {

    @Query("SELECT new org.example.datn_website_supershoes.dto.response.BillDetailResponse(" +
            "bd.id, p.imageByte, p.name, c.name, bd.quantity, b.totalAmount, b.status) " +
            "FROM BillDetail bd " +
            "JOIN bd.productDetail pd " +
            "JOIN pd.product p " +
            "JOIN pd.color c " +
            "JOIN bd.bill b " +
            "WHERE b.codeBill = :codeBill " +
            "ORDER BY pd.updatedAt DESC ")
    List<BillDetailResponse> listBillDetailResponseByCodeBill(@Param("codeBill") String codeBill);

    // Pagination support for listing BillDetails based on specifications
    Page<BillDetail> findAll(Specification<BillDetail> spec, Pageable pageable);

    @Query("SELECT bd FROM BillDetail bd WHERE bd.id = :id")
    Optional<BillDetail> findById(@Param("id") Long id);
}
