package org.example.datn_website_supershoes.controller;

import jakarta.persistence.criteria.Predicate;
import org.example.datn_website_supershoes.dto.response.AccountResponse;
import org.example.datn_website_supershoes.dto.response.ProductDetailResponse;
import org.example.datn_website_supershoes.dto.response.ProductDetailResponseByNam;
import org.example.datn_website_supershoes.dto.response.ProductResponse;
import org.example.datn_website_supershoes.model.Product;
import org.example.datn_website_supershoes.model.ProductDetail;
import org.example.datn_website_supershoes.service.ProductDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/productDetail")
public class ProductDetailController {
    @Autowired
    private ProductDetailService productDetailService;

    @GetMapping("/list-productDetail")
    public ResponseEntity<Map<String, Object>> getAllProductDetail(
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "category", required = false) Long categoryId,
            @RequestParam(value = "brand", required = false) Long brandId,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "20") int size
    ) {  // Tạo Specification cho các tiêu chí tìm kiếm
        Specification<ProductDetail> spec = (root, query, criteriaBuilder) -> {
            Predicate p = criteriaBuilder.conjunction();

            if (status != null && !status.isEmpty()) {
                p = criteriaBuilder.and(p, criteriaBuilder.equal(root.get("status"), status));
            }
            if (name != null && !name.isEmpty()) {
                p = criteriaBuilder.and(p, criteriaBuilder.equal(root.get("name"), name));
            }
            if (categoryId != null) {
                p = criteriaBuilder.and(p, criteriaBuilder.like(root.get("category").get("id").as(String.class), "%" + categoryId + "%"));
            }
            if (brandId != null) {
                p = criteriaBuilder.and(p, criteriaBuilder.like(root.get("brand").get("id").as(String.class), "%" + brandId + "%"));
            }

            return p;
        };

        Pageable pageable = PageRequest.of(page, size);
        Page<ProductDetailResponse> productDetailList = productDetailService.getAllProductDetail(spec, pageable);
        Map<String, Object> response = new HashMap<>();
        response.put("DT", productDetailList);
        response.put("EC", 0);
        response.put("EM", "Get all productDetails succeed");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<ProductDetail> getProductById(@PathVariable Long id) {
        Optional<ProductDetail> productDetail = productDetailService.getProductByIdDetail(id);
        return productDetail.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/add")
    public ResponseEntity<Map<String, Object>> createProductDetail(@RequestBody ProductDetail productDetail) {
        ProductDetail createdProduct = productDetailService.createProductDetail(productDetail);
        Map<String, Object> response = new HashMap<>();
        response.put("DT", createdProduct);
        response.put("EC", 0);
        response.put("EM", "ProductDetail added successfully");

        return ResponseEntity.ok(response);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Map<String, Object>> updateProductDetail(@PathVariable Long id, @RequestBody ProductDetail productDetail) {
        ProductDetail updateProductDetail = productDetailService.updateProductDetail(id, productDetail);
        Map<String, Object> response = new HashMap<>();
        response.put("DT", updateProductDetail);
        response.put("EC", 0);
        response.put("EM", "ProductDetail updated successfully");

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteProductDetail(@PathVariable Long id) {
        try {
            productDetailService.deleteProductDetail(id);
            return ResponseEntity.status(HttpStatus.OK).body("ProductDetail deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting productDetail");
        }
    }

    @GetMapping("/listProductDetail")
    public ResponseEntity<?> getProductDetail(@RequestParam("idProducts") List<Long> idProducts) {
        List<ProductDetailResponseByNam> productDetails = productDetailService.findProductDetailRequests(idProducts);
        return ResponseEntity.ok(productDetails);
    }
    @GetMapping("/filterListProductDetail")
    public List<ProductDetailResponseByNam> getAllAccountSearch(
            @RequestParam("idProducts") List<Long> idProducts,
            @RequestParam("search") String search,
            @RequestParam("nameSize") String nameSize,
            @RequestParam("nameColor") String nameColor,
            @RequestParam("priceRange") String priceRange) {
        return productDetailService.filterListProductDetail(idProducts,search,nameSize,nameColor,priceRange);
    }
}
