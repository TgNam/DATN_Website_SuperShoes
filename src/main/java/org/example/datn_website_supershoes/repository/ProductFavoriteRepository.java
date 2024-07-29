package org.example.datn_website_supershoes.repository;

import org.example.datn_website_supershoes.model.ProductFavorite;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductFavoriteRepository extends JpaRepository<ProductFavorite,Long> {
}
