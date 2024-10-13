package org.example.datn_website_supershoes.repository;


import org.example.datn_website_supershoes.dto.response.ProductFavoriteRespone;
import org.example.datn_website_supershoes.model.ProductFavorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ProductFavoriteRepository extends JpaRepository<ProductFavorite,Long> {
    @Query("SELECT new org.example.datn_website_supershoes.dto.response.ProductFavoriteRespone(" +
            "pf.id, " +
            "p.id, p.name, a.id, a.name, pf.status) " +
            "FROM ProductFavorite pf " +
            "JOIN pf.product p " +
            "JOIN pf.account a " +
            "WHERE pf.status = :status")
    List<ProductFavoriteRespone> findProductFavoriteRequestsByStatus(@Param("status") String status);
}
