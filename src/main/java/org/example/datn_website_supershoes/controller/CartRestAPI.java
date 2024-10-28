package org.example.datn_website_supershoes.controller;

import org.example.datn_website_supershoes.dto.response.CartResponse;
import org.example.datn_website_supershoes.dto.response.Response;
import org.example.datn_website_supershoes.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartRestAPI {

    @Autowired
    private CartService cartService;
    @GetMapping("/find")
    public ResponseEntity<?> findByAccountId() {
        try {
            Long id = 6L;
            CartResponse cart = cartService.getCartResponseByAccountId(id);
            return ResponseEntity.ok(cart);
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
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        try {
            cartService.deleteCartById(id);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(Response.builder()
                            .status(HttpStatus.OK.toString())
                            .mess("Delete success")
                            .build()
                    );
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Response.builder()
                            .status(HttpStatus.NOT_FOUND.toString())
                            .mess(e.getMessage())
                            .build()
                    );
        }
    }
}
