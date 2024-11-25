package org.example.datn_website_supershoes.controller;
;
import org.example.datn_website_supershoes.dto.request.ProductRequest;
import org.example.datn_website_supershoes.dto.response.*;
import org.example.datn_website_supershoes.service.ProductDetailService;
import org.example.datn_website_supershoes.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    ProductDetailService productDetailService;

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
        return productService.filterProductProductDetailResponse(search,idCategory,idBrand,status);
    }

    @PostMapping("/addProduct")
    public ResponseEntity<?> createProduct(@RequestBody ProductRequest productRequest) {
        try {
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

    @GetMapping("/productImage")
    public ProductImageResponse findImageByIdProduct(@RequestParam(value = "idProduct", required = false) Long id) {
        if (id==null){
            id = 0L;
        }
        return productService.findImageByIdProduct(id);
    }


    // dùng cho sale sản phẩm
    @GetMapping("/listProduct")
    public List<ProductResponse> getAllAccount() {
        List<ProductResponse> productResponse = productService.findProductRequests();
        return productResponse;
    }

    @GetMapping("/listProductSearch")
    private List<ProductResponse> findSearch(@RequestParam("search") String search) {
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
