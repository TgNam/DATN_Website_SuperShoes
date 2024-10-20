package org.example.datn_website_supershoes.repository;

import org.example.datn_website_supershoes.dto.response.PromotionResponse;
import org.example.datn_website_supershoes.model.Promotion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PromotionRepository extends JpaRepository<Promotion, Long> {

    @Query("""
            SELECT new org.example.datn_website_supershoes.dto.response.PromotionResponse(
            p.id, p.codePromotion, p.name, p.value, p.type, p.note,
            p.startAt, p.endAt, p.status)
            FROM Promotion p WHERE p.status = :status
            """)
    List<PromotionResponse> listPromotionResponseByStatus(@Param("status") String status);
    @Query("""
            SELECT new org.example.datn_website_supershoes.dto.response.PromotionResponse(
            p.id, p.codePromotion, p.name, p.value, p.type, p.note,
            p.startAt, p.endAt, p.status)
            FROM Promotion p
            """)
    List<PromotionResponse> listPromotionResponse();
    @Query("SELECT d FROM Promotion d WHERE d.endAt < :currentTime AND d.status in(:status)")
    List<Promotion> findFinishedDiscounts(@Param("currentTime") LocalDateTime currentTime,List<String> status);
    @Query("SELECT d FROM Promotion d WHERE d.startAt < :currentTime AND d.endAt > :currentTime AND d.status = :status")
    List<Promotion> findUpcomingDiscounts(@Param("currentTime") LocalDateTime currentTime,String status);
    @Query("SELECT d FROM Promotion d WHERE d.id = :id  AND d.status in(:status)")
    Optional<Promotion> findPromotionByIdAndStatus(@Param("id") Long id, List<String> status);


}
