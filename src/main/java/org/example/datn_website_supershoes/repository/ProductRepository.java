package org.example.datn_website_supershoes.repository;

import org.example.datn_website_supershoes.dto.response.ProductImageResponse;
import org.example.datn_website_supershoes.dto.response.ProductProductDetailResponse;
import org.example.datn_website_supershoes.dto.response.ProductResponse;
import org.example.datn_website_supershoes.model.Product;
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
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    @Query("select new org.example.datn_website_supershoes.dto.response.ProductResponse(" +
            "p.id, p.name, p.productCode, p.imageByte, p.gender, b.id, b.name, c.id, c.name, m.id, m.name, s.id, s.name, p.status" +
            ")" +
            "from Product p " +
            "join p.brand b join p.category c join p.material m join p.shoeSole s")
    List<ProductResponse> findProductRequests();

    @Query("""
        select new org.example.datn_website_supershoes.dto.response.ProductProductDetailResponse(
        p.id, p.name, p.productCode, p.imageByte, p.gender, b.id, b.name,
        c.id, c.name, m.id, m.name, ss.id, ss.name, sum(pd.quantity), p.status
        )
        from Product p 
        inner join p.productDetails pd
        inner join p.brand b 
        inner join p.category c 
        inner join p.material m 
        inner join p.shoeSole ss
        group by p.id, p.name, p.productCode, p.imageByte, p.gender, 
                 b.id, b.name, c.id, c.name, m.id, m.name, ss.id, ss.name, p.status
        order by p.name desc 
        """)
    List<ProductProductDetailResponse> findProductProductDetailResponse();


    @Query("""
            SELECT new org.example.datn_website_supershoes.dto.response.ProductImageResponse(
            p.imageByte)  FROM Product p WHERE p.id = :id
            """)
    ProductImageResponse findImageByIdProduct(@Param("id") Long id);
}
