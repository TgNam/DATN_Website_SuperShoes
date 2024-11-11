package org.example.datn_website_supershoes.repository;

import org.example.datn_website_supershoes.dto.response.ProductViewCustomerReponse;
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
            "pro.id, pro.codePromotion,pro.value, pro.endAt, prod.id, prod.quantity) " +
            "FROM ProductDetail pd " +
            "INNER JOIN pd.product p " +
            "INNER JOIN pd.color c " +
            "INNER JOIN pd.size s " +
            "LEFT JOIN pd.promotionDetail prod ON prod.status = 'ONGOING' " +
            "LEFT JOIN prod.promotion pro " +
            "WHERE p.status = 'ACTIVE' AND pd.status = 'ACTIVE'")
    List<ProductPromotionResponse> findProductPromotion();

    @Query("SELECT NEW org.example.datn_website_supershoes.dto.response.ProductPromotionResponse(" +
            "p.id, p.name, c.id, c.name, s.id, s.name, pd.id, pd.quantity, pd.price, " +
            "pro.id, pro.codePromotion,pro.value, pro.endAt, prod.id, prod.quantity) " +
            "FROM ProductDetail pd " +
            "INNER JOIN pd.product p " +
            "INNER JOIN pd.color c " +
            "INNER JOIN pd.size s " +
            "JOIN pd.promotionDetail prod ON prod.status = 'ONGOING' " +
            "JOIN prod.promotion pro " +
            "WHERE p.status = 'ACTIVE' AND pd.status = 'ACTIVE' AND pd.id=:idProductDetail")
    Optional<ProductPromotionResponse> findProductPromotionByIdProductDetail(@Param("idProductDetail") Long idProductDetail);

    @Query("SELECT new org.example.datn_website_supershoes.dto.response.ProductViewCustomerReponse(" +
            "p.id, " +
            "p.name, " +
            "pd.price," +
            "MIN(pd.price), " +
            "MAX(pd.price), " +
            "MIN(CASE WHEN pro.status = 'ONGOING' THEN (pd.price * (1 - pro.value / 100)) ELSE pd.price END), " +
            "MAX(CASE WHEN pro.status = 'ONGOING' THEN (pd.price * (1 - pro.value / 100)) ELSE pd.price END)) " +
            "FROM ProductDetail pd " +
            "INNER JOIN pd.product p " +
            "LEFT JOIN pd.promotionDetail prod ON prod.productDetail.id = pd.id " +
            "LEFT JOIN prod.promotion pro " +
            "WHERE p.status = 'ACTIVE' " +
            "AND pd.status = 'ACTIVE' " +
            "GROUP BY p.id, p.name, pd.price "+
            "ORDER BY p.name")
    List<ProductViewCustomerReponse> findProductPriceRangeWithPromotion();

}