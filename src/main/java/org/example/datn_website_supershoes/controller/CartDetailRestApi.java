package org.example.datn_website_supershoes.controller;

import org.example.datn_website_supershoes.dto.request.CartDetailRequest;
import org.example.datn_website_supershoes.dto.response.CartDetailProductDetailResponse;
import org.example.datn_website_supershoes.dto.response.CartDetailResponse;
import org.example.datn_website_supershoes.dto.response.Response;
import org.example.datn_website_supershoes.model.Cart;
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
    CartDetailService cartDetailService;

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
    @PostMapping("/add-cartlocal-to-cart/{idUser}")
    public ResponseEntity<?> createCartDetailByCartLocal(@RequestBody CartDetailRequest cartDetailRequest,
                                              @PathVariable("idUser") long id) {
        try {
            return ResponseEntity.ok(cartDetailService.addCartLocalToCart(cartDetailRequest, id));
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
    @GetMapping("/get-cartDetail-by-account/{accountId}")
    public ResponseEntity<?> getCart(@PathVariable("accountId") long accountId){
        try {
            List<CartDetailProductDetailResponse> cartDetailByAccountId = cartDetailService.getCartDetailByAccountId(accountId);
            return ResponseEntity.ok(cartDetailByAccountId);
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
    @GetMapping("/get-cartDetail-by-accountAndListCartDetail")
    public ResponseEntity<?> getCartDetailByAccountAndListIdCartDetail(@RequestParam("accountId") Long accountId,@RequestParam("idCartDetail") List<Long> idCartDetail){
        try {
            List<CartDetailProductDetailResponse> cartDetailByAccountId = cartDetailService.getCartDetailByAccountIdAndIdCartDetail(accountId,idCartDetail);
            return ResponseEntity.ok(cartDetailByAccountId);
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

    @PostMapping("/plusCartDetail")
    public ResponseEntity<?> plusCartDetail (
            @RequestParam(value ="idCartDetail", required = false) Long idCartDetail
    ){
        try {
            if (idCartDetail == null) {
                return ResponseEntity.badRequest().body(
                        Response.builder()
                                .status(HttpStatus.BAD_REQUEST.toString())
                                .mess("Lỗi: ID hóa đơn chi tiết không được để trống!")
                                .build()
                );
            }
            return ResponseEntity
                    .ok(cartDetailService.plusCartDetail(idCartDetail));
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
    @PostMapping("/subtractCartDetail")
    public ResponseEntity<?> subtractCartDetail (
            @RequestParam(value ="idCartDetail", required = false) Long idCartDetail
    ){
        try {
            if (idCartDetail == null) {
                return ResponseEntity.badRequest().body(
                        Response.builder()
                                .status(HttpStatus.BAD_REQUEST.toString())
                                .mess("Lỗi: ID hóa đơn chi tiết không được để trống!")
                                .build()
                );
            }
            return ResponseEntity
                    .ok(cartDetailService.subtractCartDetail(idCartDetail));
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
    @DeleteMapping("/deleteCartDetail")
    public ResponseEntity<?> deleteBillDetail(
            @RequestParam(value ="idCartDetail", required = false) Long idCartDetail
    ){
        try {
            if (idCartDetail == null) {
                return ResponseEntity.badRequest().body(
                        Response.builder()
                                .status(HttpStatus.BAD_REQUEST.toString())
                                .mess("Lỗi: ID hóa đơn chi tiết không được để trống!")
                                .build()
                );
            }
            cartDetailService.deleteCartDetail(idCartDetail);
            return ResponseEntity
                    .ok("Xóa thành công!");
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

