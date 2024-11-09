package org.example.datn_website_supershoes.controller;

import org.example.datn_website_supershoes.dto.request.ProductImageRequest;
import org.example.datn_website_supershoes.dto.response.ProductImageResponse;
import org.example.datn_website_supershoes.dto.response.Response;
import org.example.datn_website_supershoes.model.Color;
import org.example.datn_website_supershoes.model.ProductDetail;
import org.example.datn_website_supershoes.model.ProductImage;
import org.example.datn_website_supershoes.repository.ColorRepository;
import org.example.datn_website_supershoes.repository.ProductDetailRepository;
import org.example.datn_website_supershoes.service.ProductImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/image")
public class ProductImageRestAPI {
    @Autowired
    private ProductImageService productImageService;


    @GetMapping("/listProductImage")
    public List<ProductImageResponse> getAllProductImage() {
        List<ProductImageResponse> productImage = productImageService.findAll();
        return productImage;
//
    }

//
//    @PostMapping("/uploadImage")
//    public ResponseEntity<?> uploadImage(@RequestBody ProductImageRequest productImageRequest) {
//        try {
//            // Gọi service để lưu hình ảnh và nhận về thông tin về hình ảnh đã lưu
//
//            ProductImage imageUrl = productImageService.createProductImage(productImageRequest); // Giả sử hàm này trả về URL hoặc ID của hình ảnh
//
//            return ResponseEntity.ok(imageUrl); // Trả về URL hoặc ID hình ảnh
//        } catch (RuntimeException e) {
//            return ResponseEntity
//                    .status(HttpStatus.NOT_FOUND)
//                    .body(
//                            Response.builder()
//                                    .status(HttpStatus.NOT_FOUND.toString())
//                                    .mess(e.getMessage())
//                                    .build()
//                    );
//        }
//    }

//    @PutMapping("/updateImages")
//    public ResponseEntity<?> updateImage(@RequestBody ProductImageRequest productImageRequest) {
//        try {
//            // Check if idProductDetail is provided
//            if (productImageRequest.getIdProductDetail() == null) {
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                        .body("idProductDetail is required for updating the image.");
//            }
//
//            // Update the image using idProductDetail only
//            ProductImage updatedImage = productImageService.updateProductImageByProductDetailId(
//                    productImageRequest.getIdProductDetail(), productImageRequest
//            );
//
//            // Create a response with the updated image data
//            ProductImageResponse response = new ProductImageResponse(updatedImage.getImageByte());
//
//            return ResponseEntity.ok(response);
//
//        } catch (RuntimeException e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body("Error updating image: " + e.getMessage());
//        }
//    }

    @PostMapping("/updateImages2")
    public ResponseEntity<?> createOrUpdateImages(@RequestBody ProductImageRequest productImageRequest) {
        try {
            // Check if idProductDetail is provided
            if (productImageRequest.getIdProductDetail() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("idProductDetail is required for updating the images.");
            }

            // Check if imageBytes list is provided
            if (productImageRequest.getImageBytes() == null || productImageRequest.getImageBytes().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("imageBytes are required for updating the images.");
            }

            // Call the service method to create or update images
            List<ProductImage> updatedImages = productImageService.updateProductImageByProductDetailId(
                    productImageRequest.getIdProductDetail(), productImageRequest
            );

            // Convert each ProductImage to a ProductImageResponse
            List<ProductImageResponse> responses = updatedImages.stream()
                    .map(image -> new ProductImageResponse(image.getImageByte()))
                    .collect(Collectors.toList());

            return ResponseEntity.ok(responses);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating images: " + e.getMessage());
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
