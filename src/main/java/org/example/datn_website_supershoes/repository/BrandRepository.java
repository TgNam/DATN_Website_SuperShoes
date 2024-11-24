package org.example.datn_website_supershoes.repository;

import org.example.datn_website_supershoes.dto.response.BrandResponse;
import org.example.datn_website_supershoes.model.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface BrandRepository extends JpaRepository<Brand,Long> {
    @Query(value = "SELECT new org.example.datn_website_supershoes.dto.response.BrandResponse(b.id, b.name, b.status)FROM Brand b")
    List<BrandResponse> findAllBrand();
    @Query(value = "SELECT new org.example.datn_website_supershoes.dto.response.BrandResponse(b.id, b.name, b.status)FROM Brand b WHERE b.status=:status")
    List<BrandResponse> findByStatus(@Param("status") String status);

    Optional<Brand> findByName(String name);

    Optional<Brand> findByIdAndStatus(Long id, String status);
}
