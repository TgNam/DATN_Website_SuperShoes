package org.example.datn_website_supershoes.repository;

import org.example.datn_website_supershoes.model.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BrandRepository extends JpaRepository<Brand,Long> {
}
