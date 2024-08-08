package org.example.datn_website_supershoes.repository;

import org.example.datn_website_supershoes.dto.response.CartDetailResponse;
import org.example.datn_website_supershoes.model.CartDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CartDetailRepository extends JpaRepository<CartDetail, Long> {

    @Query("""
            SELECT new org.example.datn_website_supershoes.dto.response.CartDetailResponse(
            cd.id, cd.codeCart, cd.quantity, c.id, p.id, cd.status)
            FROM CartDetail cd JOIN Cart c ON cd.cart.id = c.id
            JOIN ProductDetail p ON cd.productDetail.id = p.id
            WHERE cd.status = :status
            """)
    List<CartDetailResponse> listCartDetailResponseByStatus(@Param("status") String status);
}
