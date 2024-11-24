package org.example.datn_website_supershoes.repository;

import org.example.datn_website_supershoes.dto.response.CategoryResponse;
import org.example.datn_website_supershoes.model.Brand;
import org.example.datn_website_supershoes.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface CategoryRepository extends JpaRepository<Category,Long> {
    @Query(value = "SELECT new org.example.datn_website_supershoes.dto.response.CategoryResponse(c.id, c.name, c.status)FROM Category c")
    List<CategoryResponse> findAllCategory();
    @Query(value = "SELECT new org.example.datn_website_supershoes.dto.response.CategoryResponse(c.id, c.name, c.status)FROM Category c WHERE c.status=:status")
    List<CategoryResponse> findByStatus(@Param("status") String status);

    Optional<Category> findByName(String name);
    Optional<Category> findByIdAndStatus(Long id, String status);
}
