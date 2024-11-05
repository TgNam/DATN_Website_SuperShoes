package org.example.datn_website_supershoes.repository;

import org.example.datn_website_supershoes.dto.response.ProductDetailResponse;
import org.example.datn_website_supershoes.dto.response.ProductDetailResponseByNam;
import org.example.datn_website_supershoes.dto.response.ProductPromotionResponse;
import org.example.datn_website_supershoes.model.ProductDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface ProductDetailRepository extends JpaRepository<ProductDetail, Long>, JpaSpecificationExecutor<ProductDetail> {
//    @Query("SELECT new org.example.datn_website_supershoes.dto.response.ProductDetailResponse(" +
//            "pd.id, pd.quantity, pd.price, p.id, p.name, s.id, s.name, c.id, c.name, " +
//            "p.imageByte, p.gender, b.id, b.name, ca.id, ca.name, m.id, m.name, ss.id, ss.name, pd.status) " +
//            "FROM ProductDetail pd " +
//            "JOIN pd.product p " +
//            "JOIN p.brand b " +
//            "JOIN p.category ca " +
//            "JOIN p.material m " +
//            "JOIN p.shoeSole ss " +
//            "JOIN pd.color c " +
//            "JOIN pd.size s " +
//            "WHERE pd.status = :status " +
//            "AND (:categoryId IS NULL OR ca.id = :categoryId) " +
//            "AND (:brandId IS NULL OR b.id = :brandId) " +
//            "AND (:id IS NULL OR pd.id = :id)" +
//            "AND (:name IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))) " +
//            "AND (:categoryId IS NOT NULL OR :brandId IS NOT NULL OR :name IS NOT NULL) " + // Thêm điều kiện bắt buộc ít nhất 1 tham số khác null
//            "GROUP BY pd.id, pd.quantity, pd.price, p.id, p.name, s.id, s.name, c.id, c.name," +
//            "p.imageByte, p.gender, b.id, b.name, ca.id, ca.name, m.id, m.name, ss.id, ss.name, pd.status")
//    List<ProductDetailResponse> findProductDetailRequestsByStatus(@Param("status") String status,
//                                                                  @Param("categoryId") Long categoryId,
//                                                                  @Param("brandId") Long brandId,
//                                                                  @Param("id") Long id,
//                                                                  @Param("name") String name);


    Page<ProductDetail> findAll(Specification<ProductDetail> spec, Pageable pageable);


    @Query("SELECT new org.example.datn_website_supershoes.dto.response.ProductDetailResponseByNam(" +
            "pd.id, pd.quantity, pd.price, p.id, p.name, s.id, s.name, c.id, c.name, c.codeColor, pd.status) " +
            "FROM ProductDetail pd " +
            "JOIN pd.product p " +
            "JOIN pd.color c " +
            "JOIN pd.size s " +
            "where p.id IN :idProducts")
    List<ProductDetailResponseByNam> findProductDetailRequests(@Param("idProducts") List<Long> idProducts);
    Optional<ProductDetail> findById(Long idProductDetail);

    Optional<ProductDetail> findByIdAndAndStatus(Long idProductDetail,String Status);
    @Query("SELECT NEW org.example.datn_website_supershoes.dto.response.ProductPromotionResponse(" +
            "p.id, p.name, c.id, c.name, s.id, s.name, pd.id, pd.quantity, pd.price, " +
            "pro.id, pro.codePromotion, pro.endAt, prod.id, prod.promotionPrice, prod.quantity) " +
            "FROM ProductDetail pd " +
            "INNER JOIN pd.product p " +
            "INNER JOIN pd.color c " +
            "INNER JOIN pd.size s " +
            "LEFT JOIN pd.promotionDetail prod ON prod.status = 'ONGOING' " +
            "LEFT JOIN prod.promotion pro " +
            "WHERE p.status = 'ACTIVE' AND pd.status = 'ACTIVE'")
    List<ProductPromotionResponse> findProductPromotion();

}
