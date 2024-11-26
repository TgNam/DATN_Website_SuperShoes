package org.example.datn_website_supershoes.repository;


import jakarta.transaction.Transactional;
import org.example.datn_website_supershoes.dto.response.ProductImageResponse;
import org.example.datn_website_supershoes.model.ProductDetail;
import org.example.datn_website_supershoes.model.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
    @Query("""
                SELECT new org.example.datn_website_supershoes.dto.response.ProductImageResponse(
                p.imageByte) 
            FROM ProductImage p  WHERE p.productDetail.id = :id
                """)
    List<ProductImageResponse> findListImageByIdProductDetail(@Param("id") Long id);

    @Query(value = """
            SELECT new org.example.datn_website_supershoes.dto.response.ProductImageResponse(
                p.imageByte) 
            FROM ProductImage p 
            """)
    List<ProductImageResponse> listProductImageResponse();

    @Transactional
    void deleteByProductDetail(ProductDetail productDetail);
}
