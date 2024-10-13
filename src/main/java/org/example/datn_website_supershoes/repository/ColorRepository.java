package org.example.datn_website_supershoes.repository;

import org.example.datn_website_supershoes.dto.response.ColorResponse;
import org.example.datn_website_supershoes.dto.response.SizeResponse;
import org.example.datn_website_supershoes.model.Color;
import org.example.datn_website_supershoes.model.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ColorRepository extends JpaRepository<Color,Long> {
    @Query(value = "SELECT new org.example.datn_website_supershoes.dto.response.ColorResponse(c.id, c.name, c.codeColor, c.status)FROM Color c")
    List<ColorResponse> findAllColor();
    @Query(value = "SELECT new org.example.datn_website_supershoes.dto.response.ColorResponse(" +
            "c.id, c.name, c.codeColor, c.status)" +
            "FROM Color c where c.status=:status")
    List<ColorResponse> findColorByStatus(@Param("status") String status);
    Optional<Color> findByName(String name);
    Optional<Color> findByCodeColor(String codeColor);
}
