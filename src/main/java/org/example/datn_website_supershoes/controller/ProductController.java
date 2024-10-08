package org.example.datn_website_supershoes.controller;

import jakarta.persistence.criteria.Predicate;
import org.example.datn_website_supershoes.dto.response.AccountResponse;
import org.example.datn_website_supershoes.dto.response.ColorResponse;
import org.example.datn_website_supershoes.dto.response.ProductResponse;
import org.example.datn_website_supershoes.dto.response.SizeResponse;
import org.example.datn_website_supershoes.model.Product;
import org.example.datn_website_supershoes.repository.BrandRepository;
import org.example.datn_website_supershoes.repository.CategoryRepository;
import org.example.datn_website_supershoes.repository.MaterialRepository;
import org.example.datn_website_supershoes.repository.ProductRepository;
import org.example.datn_website_supershoes.repository.ShoeSoleRepository;
import org.example.datn_website_supershoes.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private MaterialRepository materialRepository;

    @Autowired
    private ShoeSoleRepository shoeSoleRepository;
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
                // Sử dụng LIKE để tìm kiếm từng ký tự một
                p = criteriaBuilder.and(p, criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
            }
            if (categoryId != null) {
                p = criteriaBuilder.and(p, criteriaBuilder.equal(root.get("category").get("id"), categoryId));
            }
            if (brandId != null) {
                p = criteriaBuilder.and(p, criteriaBuilder.equal(root.get("brand").get("id"), brandId));
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

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/add")
    public ResponseEntity<Map<String, Object>> createProduct(@RequestBody Product product) {
        System.out.println("Product received: " + product);

        ProductResponse createdProduct = productService.createProduct(product);
        System.out.println("Created product response: " + createdProduct);

        if (createdProduct == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("message", "Unable to create product"));
        }

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

    // dùng cho sale sản phẩm
    @GetMapping("/listProduct")
    public List<ProductResponse> getAllAccount() {
        List<ProductResponse> productResponse = productService.findProductRequests();
        return productResponse;
    }
    @GetMapping("/listProductSearch")
    private List<ProductResponse> findSearch(@RequestParam("search") String search){
        return productService.findProductRequests().stream()
                .filter(ProductResponse -> ProductResponse.getName().toLowerCase().contains(search.trim().toLowerCase()))
                .collect(Collectors.toList());
    }
}
