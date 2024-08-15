package org.example.datn_website_supershoes.repository;

import org.example.datn_website_supershoes.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category,Long> {
}
