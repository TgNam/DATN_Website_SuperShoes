package org.example.datn_website_supershoes.controller;

import org.example.datn_website_supershoes.dto.response.ProductFavoriteRespone;
import org.example.datn_website_supershoes.model.ProductFavorite;
import org.example.datn_website_supershoes.service.ProductFavoriteService;
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
@RequestMapping("/productFavorite")
public class ProductFavoriteController {

    @Autowired
    ProductFavoriteService productFavoriteService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllProductFavorite() {
        List<ProductFavoriteRespone> productFavoriteList = productFavoriteService.getAllProductFavorite();
        Map<String, Object> response = new HashMap<>();
        response.put("DT", productFavoriteList);
        response.put("EC", 0);
        response.put("EM", "Get all productFavorites succeed");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<ProductFavorite> getProductFavoriteById(@PathVariable Long id) {
        Optional<ProductFavorite> productFavorite = productFavoriteService.getProductFavoriteById(id);
        return productFavorite.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/add")
    public ResponseEntity<Map<String, Object>> createProductFavorite(@RequestBody ProductFavorite productFavorite) {
        ProductFavorite productFavoriteList = productFavoriteService.createProductFavorite(productFavorite);
        Map<String, Object> response = new HashMap<>();
        response.put("DT", productFavoriteList);
        response.put("EC", 0);
        response.put("EM", "Get all productFavorites succeed");

        return ResponseEntity.ok(response);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Map<String, Object>> updateProductFavorite(@PathVariable Long id, @RequestBody ProductFavorite productFavorite) {
        ProductFavorite productFavoriteList = productFavoriteService.updateProductFavorite(id, productFavorite);
        Map<String, Object> response = new HashMap<>();
        response.put("DT", productFavoriteList);
        response.put("EC", 0);
        response.put("EM", "Get all productFavorites succeed");

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteProductFavorites(@PathVariable Long id) {
        try {
            productFavoriteService.deleteProductFavorite(id);
            return ResponseEntity.status(HttpStatus.OK).body("ProductFavorites deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting productFavorites");
        }
    }
}
