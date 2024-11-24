package org.example.datn_website_supershoes.repository;

import org.example.datn_website_supershoes.dto.response.ShoeSoleResponse;
import org.example.datn_website_supershoes.model.ShoeSole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface ShoeSoleRepository extends JpaRepository<ShoeSole,Long> {
    @Query(value = "SELECT new org.example.datn_website_supershoes.dto.response.ShoeSoleResponse(s.id, s.name, s.status)FROM ShoeSole s")
    List<ShoeSoleResponse> findAllShoeSole();
    @Query(value = "SELECT new org.example.datn_website_supershoes.dto.response.ShoeSoleResponse(s.id, s.name, s.status)FROM ShoeSole s WHERE s.status=:status")
    List<ShoeSoleResponse> findByStatus(@Param("status") String status);

    Optional<ShoeSole> findByName(String name);
    Optional<ShoeSole> findByIdAndStatus(Long id, String status);
}
