package org.example.datn_website_supershoes.repository;

import org.example.datn_website_supershoes.dto.response.SizeResponse;
import org.example.datn_website_supershoes.model.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SizeRepository extends JpaRepository<Size, Long> {
    @Query(value = "SELECT new org.example.datn_website_supershoes.dto.response.SizeResponse(s.id, s.name, s.status)FROM Size s")
    List<SizeResponse> findByStatus();

    Optional<Size> findByName(String name);
}
