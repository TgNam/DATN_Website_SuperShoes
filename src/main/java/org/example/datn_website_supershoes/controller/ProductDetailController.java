package org.example.datn_website_supershoes.controller;

import org.example.datn_website_supershoes.model.ProductDetail;
import org.example.datn_website_supershoes.service.ProductDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/productDetail")
public class ProductDetailController {
    @Autowired
    private ProductDetailService productDetailService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllProductDetail() {
        List<ProductDetail> productDetailList = productDetailService.getAllProductDetail();
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
}
