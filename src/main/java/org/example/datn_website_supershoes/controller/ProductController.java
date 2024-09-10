package org.example.datn_website_supershoes.controller;

import jakarta.persistence.criteria.Predicate;
import org.example.datn_website_supershoes.dto.response.ProductResponse;
import org.example.datn_website_supershoes.model.Product;
import org.example.datn_website_supershoes.repository.ProductRepository;
import org.example.datn_website_supershoes.service.ProductService;
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

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;
    @Autowired
    private ProductRepository productRepository;

//    @GetMapping
//    public ResponseEntity<Map<String, Object>> getAllProduct() {
//        List<ProductResponse> productList = productService.getAllProduct();
//        Map<String, Object> response = new HashMap<>();
//        response.put("DT", productList);
//        response.put("EC", 0);
//        response.put("EM", "Get all products succeed");
//        return ResponseEntity.ok(response);
//    }

    @GetMapping("/list-product")
    public ResponseEntity<Map<String, Object>> getAllProduct(
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "category", required = false) Long categoryId,
            @RequestParam(value = "brand", required = false) Long brandId,
            @RequestParam(value = "name", required = false) String name,

            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "20") int size
    ) {
        // Tạo Specification cho các tiêu chí tìm kiếm
        Specification<Product> spec = (root, query, criteriaBuilder) -> {
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

    @PostMapping("/add")
    public ResponseEntity<Map<String, Object>> createProduct(@RequestBody Product product) {
        Product createdProduct = productService.createProduct(product);
        Map<String, Object> response = new HashMap<>();
        response.put("DT", createdProduct);
        response.put("EC", 0);
        response.put("EM", "Product added successfully");

        return ResponseEntity.ok(response);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Map<String, Object>> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        Product updateProduct = productService.updateProduct(id, product);
        Map<String, Object> response = new HashMap<>();
        response.put("DT", updateProduct);
        response.put("EC", 0);
        response.put("EM", "Product updated successfully");

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        try {
            productService.deleteProduct(id);
            return ResponseEntity.status(HttpStatus.OK).body("Product deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting product");
        }
    }


}
