package org.example.datn_website_supershoes.repository;


import org.example.datn_website_supershoes.dto.response.AccountResponse;
import org.example.datn_website_supershoes.dto.response.ProductImageResponse;
import org.example.datn_website_supershoes.model.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
    @Query(value = """
        SELECT new org.example.datn_website_supershoes.dto.response.ProductImageResponse(
            p.imageByte) 
        FROM ProductImage p 
        """)
    List<ProductImageResponse> listProductImageResponse();
}