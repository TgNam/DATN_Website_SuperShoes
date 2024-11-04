package org.example.datn_website_supershoes.controller;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.example.datn_website_supershoes.dto.response.ProductResponse;
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
import org.springframework.web.multipart.MultipartFile;
import java.util.UUID;

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
    private MaterialRepository materialRepository;

    @Autowired
    private ShoeSoleRepository shoeSoleRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private BrandRepository brandRepository;



    @GetMapping("/list-product")
    public ResponseEntity<Map<String, Object>> getAllProduct(
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "category", required = false) Long categoryId,
            @RequestParam(value = "brand", required = false) Long brandId,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "id", required = false) Long id,
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
        // Tìm các đối tượng theo ID

        Map<String, Object> response = new HashMap<>();
        try {

            if (product.getName() == null) {
                response.put("EC", 1);
                response.put("EM", "name is missing in Product");
                return ResponseEntity.badRequest().body(response);
            }
            if (product.getImageByte() == null) {
                response.put("EC", 1);
                response.put("EM", "ImageByte is missing in Product");
                return ResponseEntity.badRequest().body(response);
            }

            Brand brand = brandRepository.findById(product.getBrand().getId())
                    .orElseThrow(() -> new RuntimeException("Brand not found"));
            Category category = categoryRepository.findById(product.getCategory().getId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            Material material = materialRepository.findById(product.getMaterial().getId())
                    .orElseThrow(() -> new RuntimeException("Material not found"));
            ShoeSole shoeSole = shoeSoleRepository.findById(product.getShoeSole().getId())
                    .orElseThrow(() ->   new RuntimeException("ShoeSole trong Product là null"));
            // Lưu URL của ảnh thay vì file
            if (product.getImageByte() != null) {
                System.out.println("URL ảnh sản phẩm: " + product.getImageByte());
            }

            // Thiết lập các đối tượng vào product
            product.setBrand(brand);
            product.setCategory(category);
            product.setMaterial(material);
            product.setShoeSole(shoeSole);
            product.setImageByte(product.getImageByte());
            // Lưu sản phẩm
            String generatedCode = generateProductCode(); // Hàm sinh mã
            product.setProductCode(generatedCode);
            ProductResponse createdProduct = productService.createProduct(product);
            System.out.println("Created product response: " + createdProduct);


            response.put("DT", createdProduct);
            response.put("EC", 0);
            response.put("EM", "Product added successfully");

            return ResponseEntity.ok(response);
        }catch (RuntimeException e) {
            // Xử lý lỗi và phản hồi
            response.put("EC", 1);
            response.put("EM", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    private String generateProductCode() {
        // Sử dụng UUID để tạo một mã duy nhất
        return UUID.randomUUID().toString();
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Map<String, Object>> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        // Tìm sản phẩm hiện có trong cơ sở dữ liệu
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Cập nhật thông tin thương hiệu nếu có trong yêu cầu
        if (product.getBrand() != null && product.getBrand().getId() != null) {
            Brand brand = brandRepository.findById(product.getBrand().getId())
                    .orElseThrow(() -> new RuntimeException("Brand not found"));
            existingProduct.setBrand(brand);
        }

        // Cập nhật thông tin danh mục nếu có trong yêu cầu
        if (product.getCategory() != null && product.getCategory().getId() != null) {
            Category category = categoryRepository.findById(product.getCategory().getId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            existingProduct.setCategory(category);
        }

        // Cập nhật thông tin chất liệu nếu có trong yêu cầu
        if (product.getMaterial() != null && product.getMaterial().getId() != null) {
            Material material = materialRepository.findById(product.getMaterial().getId())
                    .orElseThrow(() -> new RuntimeException("Material not found"));
            existingProduct.setMaterial(material);
        }

        // Cập nhật thông tin đế giày nếu có trong yêu cầu
        if (product.getShoeSole() != null && product.getShoeSole().getId() != null) {
            ShoeSole shoeSole = shoeSoleRepository.findById(product.getShoeSole().getId())
                    .orElseThrow(() -> new RuntimeException("ShoeSole not found"));
            existingProduct.setShoeSole(shoeSole);
        }

        // Xử lý ảnh nếu có thay đổi
        if (product.getImageByte() != null && product.getImageByte().length > 0) {
            existingProduct.setImageByte(product.getImageByte());
        }

        // Cập nhật các trường thông tin sản phẩm, giữ nguyên giá trị cũ nếu không có thay đổi
        if (product.getName() != null) {
            existingProduct.setName(product.getName());
        }
        if (product.getProductCode() != null) {
            existingProduct.setProductCode(product.getProductCode());
        }
        if (product.getStatus() != null) {
            existingProduct.setStatus(product.getStatus());
        }

        // Lưu sản phẩm đã cập nhật
        Product updatedProduct = productRepository.save(existingProduct);

        // Tạo phản hồi JSON
        Map<String, Object> response = new HashMap<>();
        response.put("DT", updatedProduct);
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
