package org.example.datn_website_supershoes.repository;

import org.example.datn_website_supershoes.dto.response.BrandResponse;
import org.example.datn_website_supershoes.model.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BrandRepository extends JpaRepository<Brand,Long> {
    @Query(value = "SELECT new org.example.datn_website_supershoes.dto.response.BrandResponse(b.id, b.name, b.status)FROM Brand b")
    List<BrandResponse> findByStatus();

    Optional<Brand> findByName(String name);
}
