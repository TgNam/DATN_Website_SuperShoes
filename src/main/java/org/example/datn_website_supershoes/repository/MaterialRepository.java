package org.example.datn_website_supershoes.repository;

import org.example.datn_website_supershoes.model.Material;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MaterialRepository extends JpaRepository<Material,Long> {
}
