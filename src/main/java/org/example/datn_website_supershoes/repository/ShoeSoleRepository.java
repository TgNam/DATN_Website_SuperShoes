package org.example.datn_website_supershoes.repository;

import org.example.datn_website_supershoes.dto.response.ShoeSoleResponse;
import org.example.datn_website_supershoes.dto.response.SizeResponse;
import org.example.datn_website_supershoes.model.ShoeSole;
import org.example.datn_website_supershoes.model.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface ShoeSoleRepository extends JpaRepository<ShoeSole,Long> {
    @Query(value = "SELECT new org.example.datn_website_supershoes.dto.response.ShoeSoleResponse(s.id, s.name, s.status)FROM ShoeSole s")
    List<ShoeSoleResponse> findByStatus();

    Optional<ShoeSole> findByName(String name);
}
