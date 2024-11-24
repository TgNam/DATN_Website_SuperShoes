package org.example.datn_website_supershoes.controller;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.example.datn_website_supershoes.dto.request.ProductRequest;
import org.example.datn_website_supershoes.dto.response.ProductImageResponse;
import org.example.datn_website_supershoes.dto.response.ProductResponse;
import org.example.datn_website_supershoes.dto.response.ProductViewCustomerReponse;
import org.example.datn_website_supershoes.dto.response.Response;
import org.example.datn_website_supershoes.model.Brand;
import org.example.datn_website_supershoes.model.Category;
import org.example.datn_website_supershoes.model.Material;
import org.example.datn_website_supershoes.model.Product;
import org.example.datn_website_supershoes.model.ProductDetail;
import org.example.datn_website_supershoes.model.ShoeSole;
import org.example.datn_website_supershoes.repository.BrandRepository;
import org.example.datn_website_supershoes.repository.CategoryRepository;
import org.example.datn_website_supershoes.repository.MaterialRepository;
import org.example.datn_website_supershoes.repository.ProductRepository;
import org.example.datn_website_supershoes.repository.ShoeSoleRepository;
import org.example.datn_website_supershoes.service.ProductDetailService;
import org.example.datn_website_supershoes.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/product")
public class ProductController {

    @Autowired
    private ProductService productService;
    @Autowired
    ProductDetailService productDetailService;


    @GetMapping("/list-product")
    public ResponseEntity<Map<String, Object>> getAllProduct(
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "category", required = false) Long categoryId,
            @RequestParam(value = "brand", required = false) Long brandId,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "id", required = false) Long id,
            @RequestParam(value = "productDetailId", required = false) Long productDetailId,
            @RequestParam(value = "productCode", required = false) String productCode,
            @RequestParam(value = "size", defaultValue = "20") int size
    ) {
        // Tạo Specification cho các tiêu chí tìm kiếm
        Specification<Product> spec = (root, query, criteriaBuilder) -> {
            Predicate p = criteriaBuilder.conjunction();
            if (id != null) {
                p = criteriaBuilder.and(p, criteriaBuilder.equal(root.get("id"), id));

            }
            if (status != null && !status.isEmpty()) {
                p = criteriaBuilder.and(p, criteriaBuilder.equal(root.get("status"), status));
            }
            if (name != null && !name.isEmpty()) {
                // Sử dụng LIKE để tìm kiếm từng ký tự một
                p = criteriaBuilder.and(p, criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
            }
            if (categoryId != null) {
                p = criteriaBuilder.and(p, criteriaBuilder.equal(root.get("category").get("id"), categoryId));
            }
            if (brandId != null) {
                p = criteriaBuilder.and(p, criteriaBuilder.equal(root.get("brand").get("id"), brandId));
            }
            if (productDetailId != null) {
                Join<ProductDetail, Product> detailProductJoin = root.join("product");
                p = criteriaBuilder.and(p, criteriaBuilder.equal(detailProductJoin.get("id"), productDetailId));
            }

            return p;
        };

        Pageable pageable = PageRequest.of(page, size);
        Page<ProductResponse> productPage = productService.getAllProduct(spec, pageable);

        // Tạo phản hồi
        Map<String, Object> response = new HashMap<>();
        response.put("DT", productPage.getContent());
        response.put("totalItems", productPage.getTotalElements());
        response.put("totalPages", productPage.getTotalPages());
        response.put("currentPage", productPage.getNumber());
        response.put("EC", 0);
        response.put("EM", "Get all products succeed");

        return ResponseEntity.ok(response);
    }


    @GetMapping("/detail/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Optional<Product> product = productService.getProductById(id);
        return product.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }


    @PostMapping("/addProduct")
    public ResponseEntity<?> createProduct(@RequestBody ProductRequest productRequest) {
        try {
            productService.addProduct(productRequest);
            return ResponseEntity.ok("Thêm thành công");
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(Response.builder()
                            .status(HttpStatus.CONFLICT.toString())
                            .mess(e.getMessage())
                            .build()
                    );
        }
    }

    @GetMapping("/productImage")
    public ProductImageResponse findImageByIdProduct(@RequestParam(value = "idProduct", required = false) Long id) {
        if (id==null){
            id = 0L;
        }
        return productService.findImageByIdProduct(id);
    }


    // dùng cho sale sản phẩm
    @GetMapping("/listProduct")
    public List<ProductResponse> getAllAccount() {
        List<ProductResponse> productResponse = productService.findProductRequests();
        return productResponse;
    }

    @GetMapping("/listProductSearch")
    private List<ProductResponse> findSearch(@RequestParam("search") String search) {
        return productService.findProductRequests().stream()
                .filter(ProductResponse -> ProductResponse.getName().toLowerCase().contains(search.trim().toLowerCase()))
                .collect(Collectors.toList());
    }

    @GetMapping("/findProductPriceRangePromotion")
    public ResponseEntity<?> findProductPriceRangePromotion(@RequestParam(value = "idProduct", required = false) Long idProduct) {
        try {
            if (idProduct == null) {
                return ResponseEntity.badRequest().body(
                        Response.builder()
                                .status(HttpStatus.BAD_REQUEST.toString())
                                .mess("Lỗi: ID sản phẩm không được để trống!")
                                .build()
                );
            }
            ProductViewCustomerReponse productViewCustomerReponse = productService.getFindProductPriceRangeWithPromotionByIdProduct(idProduct);
            return ResponseEntity.ok(productViewCustomerReponse);
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(Response.builder()
                            .status(HttpStatus.CONFLICT.toString())
                            .mess(e.getMessage())
                            .build()
                    );
        }
    }

    @PostMapping("/get-name-product-by-id")
    public ResponseEntity<?> getNameById(@RequestBody List<Long> ids) {
        try {
            return ResponseEntity.ok(productService.getProductNameById(ids));
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(Response.builder()
                            .status(HttpStatus.CONFLICT.toString())
                            .mess(e.getMessage())
                            .build()
                    );
        }
    }

}
