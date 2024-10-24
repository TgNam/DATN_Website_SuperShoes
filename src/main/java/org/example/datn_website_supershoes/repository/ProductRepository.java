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
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ProductRepository extends JpaRepository<Product,Long>, JpaSpecificationExecutor<Product> {

//@Query("SELECT new org.example.datn_website_supershoes.dto.response.ProductResponse(" +
//        "p.id, p.name, p.productCode, p.imageByte, p.gender, " +
//        "b.id, b.name, c.id, c.name, m.id, m.name, s.id, s.name , p.status,pd.id,pd.quantity,pd.price) " +
//        "FROM Product p " +
//        "JOIN p.productDetails pd " +
//        "JOIN p.brand b " +
//        "JOIN p.category c " +
//        "JOIN p.material m " +
//        "JOIN p.shoeSole s " +
//        "WHERE p.status = :status " +
//        "AND (:categoryId IS NULL OR c.id = :categoryId) " +
//        "AND (:brandId IS NULL OR b.id = :brandId) " +
//        "AND (:name IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%')))\n" + // Điều kiện tìm kiếm theo tên
//        "GROUP BY p.id, p.name, p.productCode, p.imageByte, p.gender, " +
//        "b.id, b.name, c.id, c.name, m.id, m.name, s.id, s.name, p.status,pd.id,pd.quantity,pd.price")
@Query("SELECT new org.example.datn_website_supershoes.dto.response.ProductResponse(" +
        "p.id, p.name, p.productCode, p.imageByte, p.gender, " +
        "b.id, b.name, c.id, c.name, m.id, m.name, s.id, s.name, p.status " +
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


@Query("select new org.example.datn_website_supershoes.dto.response.ProductResponse(p.id, p.name, p.productCode, p.imageByte, p.gender, b.id, b.name, c.id, c.name, m.id, m.name, s.id, s.name, p.status)" +
        "from Product p " +
        "join p.brand b join p.category c join p.material m join p.shoeSole s")
List<ProductResponse> findProductRequests();

}
