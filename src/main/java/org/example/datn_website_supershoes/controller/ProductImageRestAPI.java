package org.example.datn_website_supershoes.controller;

import org.example.datn_website_supershoes.dto.request.ProductImageRequest;
import org.example.datn_website_supershoes.dto.response.ProductImageResponse;
import org.example.datn_website_supershoes.dto.response.Response;
import org.example.datn_website_supershoes.service.ProductImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/image")
public class ProductImageRestAPI {
    @Autowired
    private ProductImageService productImageService;

    @GetMapping("/listProductImage")
    public List<ProductImageResponse> getAllProductImage() {
        List<ProductImageResponse> productImage = productImageService.findAll();
        return productImage;
    }
    @PostMapping("/uploadImage")
    public ResponseEntity<?> uploadImage(@RequestBody ProductImageRequest productImageRequest) {
        try {
            productImageService.createProductImage(productImageRequest);
            return new ResponseEntity<>("Image uploaded successfully", HttpStatus.OK);
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(
                            Response.builder()
                                    .status(HttpStatus.NOT_FOUND.toString())
                                    .mess(e.getMessage())
                                    .build()
                    );
        }
    }
    @PostMapping("/uploadListImage")
    public ResponseEntity<?> uploadListImage(@RequestBody List<ProductImageRequest> productImageRequests) {
        try {
            for (ProductImageRequest productImageRequest : productImageRequests) {
                // Gọi service để lưu từng ảnh
                productImageService.createProductImage(productImageRequest);
            }
            return new ResponseEntity<>("Images uploaded successfully", HttpStatus.OK);
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(
                            Response.builder()
                                    .status(HttpStatus.NOT_FOUND.toString())
                                    .mess(e.getMessage())
                                    .build()
                    );
        }
    }


}
