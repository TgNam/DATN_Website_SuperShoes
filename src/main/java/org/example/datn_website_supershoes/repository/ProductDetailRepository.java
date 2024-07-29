package org.example.datn_website_supershoes.repository;

import org.example.datn_website_supershoes.model.ProductDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductDetailRepository extends JpaRepository<ProductDetail,Long> {
}
