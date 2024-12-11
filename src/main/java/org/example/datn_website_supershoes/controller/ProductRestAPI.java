package org.example.datn_website_supershoes.controller;

;
import jakarta.validation.Valid;
import org.example.datn_website_supershoes.dto.request.ProductRequest;
import org.example.datn_website_supershoes.dto.request.updateProduct.UpdateProductRequest;
import org.example.datn_website_supershoes.dto.response.*;
import org.example.datn_website_supershoes.model.Product;
import org.example.datn_website_supershoes.service.ProductDetailService;
import org.example.datn_website_supershoes.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/product")
public class ProductRestAPI {

    @Autowired
    ProductService productService;

    @Autowired
    ProductDetailService productDetailService;

    @GetMapping("/findProductResponseById")
    public ResponseEntity<?> findProductResponseById(@RequestParam(value = "idProduct", required = false) Long id) {
        try {
            if (id == null) {
                return ResponseEntity.badRequest().body(
                        Response.builder()
                                .status(HttpStatus.BAD_REQUEST.toString())
                                .mess("Lỗi: ID sản phẩm không được để trống!")
                                .build()
                );
            }
            return ResponseEntity.ok(productService.findProductById(id));
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

    @GetMapping("/findProductProductDetailResponse")
    public List<ProductProductDetailResponse> findProductProductDetailResponse() {
        List<ProductProductDetailResponse> productResponse = productService.findProductProductDetailResponse();
        return productResponse;
    }

    @GetMapping("/filterProductProductDetailResponse")
    public List<ProductProductDetailResponse> filterProductProductDetailResponse(
            @RequestParam("search") String search,
            @RequestParam("idCategory") String idCategory,
            @RequestParam("idBrand") String idBrand,
            @RequestParam("status") String status
    ) {
        return productService.filterProductProductDetailResponse(search, idCategory, idBrand, status);
    }

    @PostMapping("/addProduct")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createProduct(@RequestBody @Valid ProductRequest productRequest, BindingResult result) {
        try {
            if (result.hasErrors()) {
                List<String> errors = result.getAllErrors().stream()
                        .map(error -> error.getDefaultMessage())
                        .collect(Collectors.toList());
                return ResponseEntity.badRequest().body(errors);
            }
            productService.addProduct(productRequest);
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

    @PutMapping("/updateProduct")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateProduct(@RequestBody @Valid UpdateProductRequest updateProductRequest, BindingResult result) {
        try {
            if (result.hasErrors()) {
                List<String> errors = result.getAllErrors().stream()
                        .map(error -> error.getDefaultMessage())
                        .collect(Collectors.toList());
                return ResponseEntity.badRequest().body(errors);
            }
            productService.updateProduct(updateProductRequest);
            return ResponseEntity.ok("Cập nhật thành công");
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

    @GetMapping("/productImage")
    public ProductImageResponse findImageByIdProduct(@RequestParam(value = "idProduct", required = false) Long id) {
        if (id == null) {
            id = 0L;
        }
        return productService.findImageByIdProduct(id);
    }

    @PutMapping("/update-status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateStatus(
            @RequestParam(value = "id", required = false) Long id,
            @RequestParam(value = "aBoolean", required = false) boolean aBoolean
    ) {
        try {
            if (id == null) {
                return ResponseEntity.badRequest().body(
                        Response.builder()
                                .status(HttpStatus.BAD_REQUEST.toString())
                                .mess("Lỗi: ID sản phẩm không được để trống!")
                                .build()
                );
            }
            Product product = productService.updateStatus(id, aBoolean);
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

    // dùng cho sale sản phẩm
    @GetMapping("/listProduct")
    public List<ProductResponse> getAllAccount() {
        List<ProductResponse> productResponse = productService.findProductRequests();
        return productResponse;
    }

    @GetMapping("/listProductSearch")
    public List<ProductResponse> findSearch(@RequestParam("search") String search) {
        return productService.findProductRequests().stream()
                .filter(ProductResponse -> ProductResponse.getName().toLowerCase().contains(search.trim().toLowerCase()))
                .collect(Collectors.toList());
    }

    @GetMapping("/findProductPriceRangePromotion")
    public ResponseEntity<?> findProductPriceRangePromotion(@RequestParam(value = "idProduct", required = false) Long idProduct) {
        try {
            if (idProduct == null) {
                return ResponseEntity.badRequest().body(
                        Response.builder()
                                .status(HttpStatus.BAD_REQUEST.toString())
                                .mess("Lỗi: ID sản phẩm không được để trống!")
                                .build()
                );
            }
            ProductViewCustomerReponse productViewCustomerReponse = productService.getFindProductPriceRangeWithPromotionByIdProduct(idProduct);
            return ResponseEntity.ok(productViewCustomerReponse);
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

    @PostMapping("/get-name-product-by-id")
    public ResponseEntity<?> getNameById(@RequestBody List<Long> ids) {
        try {
            return ResponseEntity.ok(productService.getProductNameById(ids));
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
