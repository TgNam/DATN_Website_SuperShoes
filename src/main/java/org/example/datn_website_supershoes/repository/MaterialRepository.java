package org.example.datn_website_supershoes.repository;

import org.example.datn_website_supershoes.dto.response.MaterialResponse;
import org.example.datn_website_supershoes.model.Material;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface MaterialRepository extends JpaRepository<Material,Long> {
    @Query(value = "SELECT new org.example.datn_website_supershoes.dto.response.MaterialResponse(m.id, m.name, m.status)FROM Material m")
    List<MaterialResponse> findAllMaterial();
    @Query(value = "SELECT new org.example.datn_website_supershoes.dto.response.MaterialResponse(m.id, m.name, m.status)FROM Material m WHERE m.status=:status")
    List<MaterialResponse> findByStatus(@Param("status") String status);

    Optional<Material> findByName(String name);
    Optional<Material> findByIdAndStatus(Long id, String status);
}
