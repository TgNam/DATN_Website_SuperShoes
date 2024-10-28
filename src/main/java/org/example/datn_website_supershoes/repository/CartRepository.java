package org.example.datn_website_supershoes.repository;

import org.example.datn_website_supershoes.dto.response.CartResponse;
import org.example.datn_website_supershoes.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    @Query(value = """
            SELECT new org.example.datn_website_supershoes.dto.response.CartResponse(c.id, a.id)
            FROM Cart c JOIN Account a ON c.account.id = a.id
            WHERE a.id = :id
            """)
    Optional<CartResponse> CartResponse(@Param("id") Long id);



}
