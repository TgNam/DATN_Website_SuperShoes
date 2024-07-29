package org.example.datn_website_supershoes.repository;

import org.example.datn_website_supershoes.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product,Long> {
}
