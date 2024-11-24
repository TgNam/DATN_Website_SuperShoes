package org.example.datn_website_supershoes.controller;

import jakarta.transaction.Transactional;
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
@RequestMapping("/api/v1/image")
public class ProductImageRestAPI {
    @Autowired
    private ProductImageService productImageService;

    @GetMapping("/listAllProductImage")
    public List<ProductImageResponse> getAllProductImage() {
        List<ProductImageResponse> productImage = productImageService.findAll();
        return productImage;
    }
    @GetMapping("/listProductImage")
    public List<ProductImageResponse> findListImageByIdProductDetail(@RequestParam(value = "idProductDetail", required = false) Long id) {
        if (id==null){
            id = 0L;
        }
        return productImageService.findListImageByIdProductDetail(id);
    }
}
