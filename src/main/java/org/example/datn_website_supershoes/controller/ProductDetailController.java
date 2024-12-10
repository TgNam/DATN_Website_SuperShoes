package org.example.datn_website_supershoes.controller;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.example.datn_website_supershoes.dto.request.ProductDetailRequest;
import org.example.datn_website_supershoes.dto.response.*;
import org.example.datn_website_supershoes.model.Color;
import org.example.datn_website_supershoes.model.Product;
import org.example.datn_website_supershoes.model.ProductDetail;
import org.example.datn_website_supershoes.model.Size;
import org.example.datn_website_supershoes.repository.ColorRepository;
import org.example.datn_website_supershoes.repository.ProductRepository;
import org.example.datn_website_supershoes.repository.SizeRepository;
import org.example.datn_website_supershoes.service.ProductDetailService;
import org.example.datn_website_supershoes.service.ProductImageService;
import org.example.datn_website_supershoes.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.awt.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/productDetail")
public class ProductDetailController {

    @Autowired
    ProductDetailService productDetailService;
    @Autowired
    ProductRepository productRepository;
    @PostMapping("/addProductDetail")
    public ResponseEntity<?> addProductDetail(
            @RequestParam(value = "idProduct", required = false) Long id,
            @RequestBody List<@Valid ProductDetailRequest> productDetailRequest,
            BindingResult result) {
        try {
            if (id == null) {
                return ResponseEntity.badRequest().body(
                        Response.builder()
                                .status(HttpStatus.BAD_REQUEST.toString())
                                .mess("Lỗi: ID sản phẩm không được để trống!")
                                .build()
                );
            }
            if (productDetailRequest == null || productDetailRequest.isEmpty()) {
                return ResponseEntity.badRequest().body(
                        Response.builder()
                                .status(HttpStatus.BAD_REQUEST.toString())
                                .mess("Danh sách sản phẩm không được để trống!")
                                .build()
                );
            }
            if (result.hasErrors()) {
                List<String> errors = result.getAllErrors().stream()
                        .map(error -> error.getDefaultMessage())
                        .collect(Collectors.toList());
                return ResponseEntity.badRequest().body(errors);
            }
            Optional<Product> optionalProduct = productRepository.findById(id);
            if (optionalProduct.isEmpty()) {
                throw new RuntimeException("Sản phẩm với Id là:  " + id + " không tồn tại trong hệ thống");
            }
            productDetailService.createProductDetail(optionalProduct.get(),productDetailRequest);
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

    @GetMapping("/listProductDetail")
    public ResponseEntity<?> getProductDetail(@RequestParam("idProducts") List<Long> idProducts) {
        List<ProductDetailResponseByNam> productDetails = productDetailService.findProductDetailRequests(idProducts);
        return ResponseEntity.ok(productDetails);
    }

    @GetMapping("/listProductDetailActive")
    public ResponseEntity<?> getProductDetailActive(@RequestParam("idProducts") Long idProducts) {
        List<ProductPromotionResponse> productDetails = productDetailService.findProductDetailActiveRequests(idProducts);
        return ResponseEntity.ok(productDetails);
    }

    @PutMapping("/update-status")
    public ResponseEntity<?> updateStatus(
            @RequestParam(value = "id", required = false) Long id,
            @RequestParam(value = "aBoolean", required = false) boolean aBoolean
    ) {
        try {
            if (id == null) {
                return ResponseEntity.badRequest().body(
                        Response.builder()
                                .status(HttpStatus.BAD_REQUEST.toString())
                                .mess("Lỗi: ID sản phẩm chi tiết không được để trống!")
                                .build()
                );
            }
            ProductDetail product = productDetailService.updateStatus(id, aBoolean);
            return ResponseEntity.ok(product);
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

    @GetMapping("/filterListProductDetail")
    public List<ProductDetailResponseByNam> getAllProductDetailSearch(
            @RequestParam("idProducts") List<Long> idProducts,
            @RequestParam("search") String search,
            @RequestParam("nameSize") String nameSize,
            @RequestParam("nameColor") String nameColor,
            @RequestParam("priceRange") String priceRange) {
        return productDetailService.filterListProductDetail(idProducts, search, nameSize, nameColor, priceRange);
    }

    @GetMapping("/listProductPromotion")
    public ResponseEntity<?> getProductPromotion() {
        List<ProductPromotionResponse> productDetails = productDetailService.findProductPromotion();
        return ResponseEntity.ok(productDetails);
    }

    @GetMapping("/filterListProductPromotion")
    public List<ProductPromotionResponse> getAllProductPromotionSearch(
            @RequestParam("search") String search,
            @RequestParam("nameSize") String nameSize,
            @RequestParam("nameColor") String nameColor,
            @RequestParam("priceRange") String priceRange) {
        return productDetailService.filterListProductPromotion(search, nameSize, nameColor, priceRange);
    }

    @GetMapping("/findProductDetailByIdProductDetail")
    public ResponseEntity<?> findProductDetailByIdProductDetail(@RequestParam(value = "idProductDetail", required = false) Long idProductDetail) {
        try {
            if (idProductDetail == null) {
                return ResponseEntity.badRequest().body(
                        Response.builder()
                                .status(HttpStatus.BAD_REQUEST.toString())
                                .mess("Lỗi: Id sản phẩm chi tiết không được để trống!")
                                .build()
                );
            }
            return ResponseEntity.ok(productDetailService.findProductDetailByIdProductDetail(idProductDetail));
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

    @GetMapping("/productPriceRangePromotion")
    public ResponseEntity<List<ProductViewCustomerReponse>> getProductPriceRangeWithPromotion() {
        List<ProductViewCustomerReponse> response = productDetailService.getProductPriceRangeWithPromotion();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/productPriceRangePromotionByQuang")
    public ResponseEntity<List<ProductViewCustomerReponseByQuang>> getProductPriceRangeWithPromotionByQuang(
            @RequestParam(value = "nameProduct", required = false) String nameProduct,
            @RequestParam(value = "idColor", required = false) Long idColor,
            @RequestParam(value = "idSize", required = false) Long idSize,
            @RequestParam(value = "idBrand", required = false) Long idBrand,
            @RequestParam(value = "idCategory", required = false) Long idCategory,
            @RequestParam(value = "minPrice", required = false) BigDecimal minPrice,
            @RequestParam(value = "maxPrice", required = false) BigDecimal maxPrice,
            @RequestParam(value = "gender", required = false) Boolean gender
    ) {
        List<ProductViewCustomerReponseByQuang> response = productDetailService.getFilteredProducts(
                nameProduct, idColor, idSize, idBrand, idCategory, minPrice, maxPrice, gender
        );
        return ResponseEntity.ok(response);
    }


    @GetMapping("/findProductPromotionByIdProcuctAndIdColorAndIdSize")
    public ResponseEntity<?> findProductPromotionByIdProcuctAndIdColorAndIdSize(
            @RequestParam(value = "idProduct", required = false) Long idProduct,
            @RequestParam(value = "idColor", required = false) Long idColor,
            @RequestParam(value = "idSize", required = false) Long idSize
    ) {
        try {
            if (idProduct == null) {
                return ResponseEntity.badRequest().body(
                        Response.builder()
                                .status(HttpStatus.BAD_REQUEST.toString())
                                .mess("Lỗi: ID sản phẩm không được để trống!")
                                .build()
                );
            }
            if (idColor == null) {
                return ResponseEntity.badRequest().body(
                        Response.builder()
                                .status(HttpStatus.BAD_REQUEST.toString())
                                .mess("Lỗi: ID màu sắc không được để trống!")
                                .build()
                );
            }
            if (idSize == null) {
                return ResponseEntity.badRequest().body(
                        Response.builder()
                                .status(HttpStatus.BAD_REQUEST.toString())
                                .mess("Lỗi: ID kích cỡ không được để trống!")
                                .build()
                );
            }

            ProductPromotionResponse productPromotionResponse = productDetailService.findProductPromotionByIdProcuctAndIdColorAndIdSize(idProduct, idColor, idSize);
            return ResponseEntity.ok(productPromotionResponse);

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





