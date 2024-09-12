package org.example.datn_website_supershoes.repository;

import org.example.datn_website_supershoes.dto.response.ProductDetailResponse;
import org.example.datn_website_supershoes.dto.response.ProductResponse;
import org.example.datn_website_supershoes.model.ProductDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductDetailRepository extends JpaRepository<ProductDetail, Long> {
    @Query("SELECT new org.example.datn_website_supershoes.dto.response.ProductDetailResponse(" +
            "pd.id, pd.quantity, pd.price,  " +
            "p.id, p.name, c.id, c.name, s.id, s.name , pd.status) " +
            "FROM ProductDetail pd " +
            "JOIN pd.product p " +
            "JOIN pd.color c " +
            "JOIN pd.size s " +
            "WHERE pd.status = :status")
    List<ProductDetailResponse> findProductDetailRequestsByStatus(@Param("status") String status);
}
