package org.example.datn_website_supershoes.controller;


import org.example.datn_website_supershoes.dto.response.ProductImageResponse;
import org.example.datn_website_supershoes.service.ProductImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/api/v1/image")
public class ProductImageRestAPI {
    @Autowired
    ProductImageService productImageService;

    @GetMapping("/listAllProductImage")
    public List<ProductImageResponse> getAllProductImage() {
        List<ProductImageResponse> productImage = productImageService.findAll();
        return productImage;
    }

    @GetMapping("/listProductImage")
    public List<ProductImageResponse> findListImageByIdProductDetail(@RequestParam(value = "idProductDetail", required = false) Long id) {
        if (id == null) {
            id = 0L;
        }
        return productImageService.findListImageByIdProductDetail(id);
    }
}
