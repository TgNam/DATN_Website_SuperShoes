package org.example.datn_website_supershoes.repository;

import org.example.datn_website_supershoes.dto.response.PromotionResponse;
import org.example.datn_website_supershoes.model.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PromotionRepository extends JpaRepository<Promotion,Long> {

    @Query("""
            SELECT new org.example.datn_website_supershoes.dto.response.PromotionResponse(pmt.codePromotion,pmt.name,pmt.value,pmt.type,pmt.note,pmt.startAt,pmt.endAt,pmt.status)
            FROM Promotion  pmt 
            """)
    List<PromotionResponse> listPromotionResponseByStatus();
}
