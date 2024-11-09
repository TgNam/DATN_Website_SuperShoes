package org.example.datn_website_supershoes.repository;


import org.example.datn_website_supershoes.dto.response.ProductImageResponse;
import org.example.datn_website_supershoes.model.ProductDetail;
import org.example.datn_website_supershoes.model.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
    @Query(value = """
        SELECT new org.example.datn_website_supershoes.dto.response.ProductImageResponse(
            p.imageByte) 
        FROM ProductImage p 
        """)
    List<ProductImageResponse> listProductImageResponse();
    List<ProductImage> findAllByIdIn(Collection<Long> ids);
    Optional<ProductImage> findByProductDetailId(Long productDetailId);
    Optional<ProductImage> findByProductDetail(ProductDetail productDetail);
    void deleteByProductDetail(ProductDetail productDetail);

}
