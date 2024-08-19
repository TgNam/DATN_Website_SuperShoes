package org.example.datn_website_supershoes.repository;

import org.example.datn_website_supershoes.dto.response.CategoryResponse;
import org.example.datn_website_supershoes.model.Category;
import org.example.datn_website_supershoes.model.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category,Long> {
    @Query(value = "SELECT new org.example.datn_website_supershoes.dto.response.CategoryResponse(c.id, c.name, c.status)FROM Category c")
    List<CategoryResponse> findByStatus();

    Optional<Category> findByName(String name);
}
