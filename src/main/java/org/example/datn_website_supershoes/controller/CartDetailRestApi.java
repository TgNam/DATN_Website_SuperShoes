package org.example.datn_website_supershoes.controller;

import org.example.datn_website_supershoes.dto.request.CartDetailRequest;
import org.example.datn_website_supershoes.dto.response.CartDetailResponse;
import org.example.datn_website_supershoes.dto.response.Response;
import org.example.datn_website_supershoes.service.CartDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cart-detail")
public class CartDetailRestApi {

    @Autowired
    private CartDetailService cartDetailService;

    @GetMapping("/codeCart")
    public ResponseEntity<?> findByAccountId(@RequestParam("id") Long id) {
        try {
            List<String> codeCart = cartDetailService.listCodeCartByIdCart(id);
            return ResponseEntity.ok(codeCart);
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

    @GetMapping("/listCactDetail")
    public ResponseEntity<?> listCartDetailResponseById(@RequestParam("id") Long id, @RequestParam("codeCart") String codeCart) {
        try {
            List<CartDetailResponse> cartDetailResponses = cartDetailService.listCartDetailResponseById(id, codeCart);
            return ResponseEntity.ok(cartDetailResponses);
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

    @DeleteMapping("/deleteById")
    public ResponseEntity<?> deleteById(@RequestParam("id") Long id) {
        try {
            cartDetailService.deleteByIdCartDetail(id);
            return ResponseEntity.ok("Xóa thành công!");
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

    @PostMapping("/add-product-to-cart/{idUser}")
    public ResponseEntity<?> createCartDetail(@RequestBody CartDetailRequest cartDetailRequest,
                                              @PathVariable("idUser") long id) {
        try {
            return ResponseEntity.ok(cartDetailService.addToCart(cartDetailRequest, id));
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

