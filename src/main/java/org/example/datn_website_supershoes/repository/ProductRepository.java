package org.example.datn_website_supershoes.repository;

import org.example.datn_website_supershoes.dto.response.ProductResponse;
import org.example.datn_website_supershoes.model.Product;
import org.example.datn_website_supershoes.model.Voucher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product,Long>, JpaSpecificationExecutor<Product> {

@Query("SELECT new org.example.datn_website_supershoes.dto.response.ProductResponse(" +
        "p.id, p.name, p.productCode, p.imageByte, p.gender, " +
        "b.id, b.name, c.id, c.name, m.id, m.name, s.id, s.name, p.status" +
        ") " +
        "FROM Product p " +
        "JOIN p.brand b " +
        "JOIN p.category c " +
        "JOIN p.material m " +
        "JOIN p.shoeSole s " +
        "WHERE p.status = :status " +
        "AND (:categoryId IS NULL OR c.id = :categoryId) " +
        "AND (:brandId IS NULL OR b.id = :brandId) " +
        "AND LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))")
List<ProductResponse> findProductRequestsByStatus(
        @Param("status") String status,
        @Param("categoryId") Long categoryId,
        @Param("brandId") Long brandId,
        @Param("name") String name);

Page<Product> findAll(Specification<Product> spec, Pageable pageable);




}
