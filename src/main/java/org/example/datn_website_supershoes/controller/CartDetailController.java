package org.example.datn_website_supershoes.controller;

import org.example.datn_website_supershoes.dto.response.CartDetailResponse;
import org.example.datn_website_supershoes.model.CartDetail;
import org.example.datn_website_supershoes.service.CartDetailService;
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
@RequestMapping("/cart-detail")
public class CartDetailController {

    @Autowired
    private CartDetailService cartDetailService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllCartDetails() {
        List<CartDetailResponse> listCartDetails = cartDetailService.getAllCartDetails();
        Map<String, Object> response = new HashMap<>();
        response.put("DT", listCartDetails);
        response.put("EC", 0);
        response.put("EM", "Get all cart details succeed");

        return ResponseEntity.ok(response);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<CartDetail> getCartDetailById(@PathVariable Long id) {
        Optional<CartDetail> cartDetail = cartDetailService.getCartDetailById(id);
        return cartDetail.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/add")
    public ResponseEntity<Map<String, Object>> createCartDetail(@RequestBody CartDetail cartDetail) {
        CartDetail createdCartDetail = cartDetailService.createCartDetail(cartDetail);
        Map<String, Object> response = new HashMap<>();
        response.put("DT", createdCartDetail);
        response.put("EC", 0);
        response.put("EM", "CartDetail added successfully");

        return ResponseEntity.ok(response);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Map<String, Object>> updateCartDetail(@PathVariable Long id, @RequestBody CartDetail cartDetail) {
        CartDetail updatedCartDetail = cartDetailService.updateCartDetail(id, cartDetail);
        Map<String, Object> response = new HashMap<>();
        response.put("DT", updatedCartDetail);
        response.put("EC", 0);
        response.put("EM", "CartDetail updated successfully");

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteCartDetail(@PathVariable Long id) {
        try {
            cartDetailService.deleteCartDetail(id);
            return ResponseEntity.status(HttpStatus.OK).body("CartDetail deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting cartDetail");
        }
    }
}

