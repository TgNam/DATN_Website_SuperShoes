package org.example.datn_website_supershoes.repository;

import org.example.datn_website_supershoes.dto.response.ProductResponse;
import org.example.datn_website_supershoes.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product,Long> {
    @Query("SELECT new org.example.datn_website_supershoes.dto.response.ProductResponse(" +
            "p.id, p.name, p.productCode, p.imageByte, p.gender, " +
            "b.id, b.name, c.id, c.name, m.id, m.name, s.id, s.name , p.status) " +
            "FROM Product p " +
            "JOIN p.brand b " +
            "JOIN p.category c " +
            "JOIN p.material m " +
            "JOIN p.shoeSole s " +
            "WHERE p.status = :status")
    List<ProductResponse> findProductRequestsByStatus(@Param("status") String status);
}
