package org.example.datn_website_supershoes.controller;

import jakarta.validation.constraints.NotNull;
import org.example.datn_website_supershoes.dto.request.AccountRequest;
import org.example.datn_website_supershoes.dto.request.CartRequest;
import org.example.datn_website_supershoes.dto.response.CartResponse;
import org.example.datn_website_supershoes.dto.response.Response;
import org.example.datn_website_supershoes.model.Account;
import org.example.datn_website_supershoes.model.Cart;
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
    public ResponseEntity<?> findByAccountId(@RequestParam("id") Long id) {
        try {
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
    @PostMapping("/create")
    public ResponseEntity<?> createAccount(@RequestBody CartRequest cartRequest) {
        try {
            Cart cart = cartService.createCart(cartRequest);
            return ResponseEntity.ok("OK");
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
