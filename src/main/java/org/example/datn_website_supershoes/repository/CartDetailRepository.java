package org.example.datn_website_supershoes.repository;

import org.example.datn_website_supershoes.dto.response.CartDetailResponse;
import org.example.datn_website_supershoes.model.CartDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CartDetailRepository extends JpaRepository<CartDetail, Long> {
    @Query("""
            SELECT DISTINCT (cd.codeCart)
            FROM CartDetail cd JOIN Cart c ON cd.cart.id = c.id
            WHERE c.id = :id
            """)
    List<String> listCodeCartByIdCart(@Param("id") Long id);

    @Query("""
            SELECT new org.example.datn_website_supershoes.dto.response.CartDetailResponse(
            cd.id, cd.codeCart, cd.quantity, c.id, p.id, cd.status)
            FROM CartDetail cd JOIN Cart c ON cd.cart.id = c.id
            JOIN ProductDetail p ON cd.productDetail.id = p.id
            WHERE cd.cart.id = :id and cd.codeCart = :codeCart
            """)
    List<CartDetailResponse> listCartDetailResponseById(@Param("id") Long id,@Param("codeCart") String codeCart);

    @Modifying
    @Transactional
    @Query("UPDATE CartDetail set quantity = :quantity where cart.id = :id and codeCart = :codeCart")
    void updateById(@Param("quantity") Integer quantity,@Param("id") Long id,@Param("codeCart") String codeCart);

    @Modifying
    @Transactional
    @Query("DELETE FROM CartDetail cd WHERE cd.cart.id = :id")
    void deleteById(@Param("id") Long id);

}
