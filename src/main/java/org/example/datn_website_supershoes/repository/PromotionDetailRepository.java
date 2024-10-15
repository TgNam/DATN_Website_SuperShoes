package org.example.datn_website_supershoes.repository;

import org.example.datn_website_supershoes.model.PromotionDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PromotionDetailRepository extends JpaRepository<PromotionDetail,Long> {
    @Query("select p from PromotionDetail p where p.productDetail.id=:idProductDetail and p.status IN (:statuses)")
    Optional<PromotionDetail> findPromotionDetailByIdProductDetailAndStatuses(@Param("idProductDetail") Long idProductDetail, @Param("statuses") List<String> statuses);
    @Query("select p from PromotionDetail p where p.promotion.id = :idPromotion and p.status IN (:statuses)")
    List<PromotionDetail> findPromotionDetailByIdPromotionAndStatuses(@Param("idPromotion") Long idPromotion, @Param("statuses") List<String> statuses);
}
