package org.example.datn_website_supershoes.controller;

import org.example.datn_website_supershoes.dto.response.CartResponse;
import org.example.datn_website_supershoes.model.Cart;
import org.example.datn_website_supershoes.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllCarts() {
        List<CartResponse> listCarts = cartService.getAllCarts();
        Map<String, Object> response = new HashMap<>();
        response.put("DT", listCarts);
        response.put("EC", 0);
        response.put("EM", "Get all carts succeed");

        return ResponseEntity.ok(response);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<Cart> getCartById(@PathVariable Long id) {
        Optional<Cart> cart = cartService.getCartById(id);
        return cart.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/add")
    public ResponseEntity<Map<String, Object>> createCart(@RequestBody Cart cart) {
        Cart createdCart = cartService.createCart(cart);
        Map<String, Object> response = new HashMap<>();
        response.put("DT", createdCart);
        response.put("EC", 0);
        response.put("EM", "Cart added successfully");

        return ResponseEntity.ok(response);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Map<String, Object>> updateCart(@PathVariable Long id, @RequestBody Cart cartDetails) {
        Cart updatedCart = cartService.updateCart(id, cartDetails);
        Map<String, Object> response = new HashMap<>();
        response.put("DT", updatedCart);
        response.put("EC", 0);
        response.put("EM", "Cart updated successfully");

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteCart(@PathVariable Long id) {
        try {
            cartService.deleteCart(id);
            return ResponseEntity.status(HttpStatus.OK).body("Cart deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting cart");
        }
    }
}
