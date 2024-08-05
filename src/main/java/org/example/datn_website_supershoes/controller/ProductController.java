package org.example.datn_website_supershoes.controller;

import org.example.datn_website_supershoes.dto.request.ProductRequest;
import org.example.datn_website_supershoes.model.Product;
import org.example.datn_website_supershoes.service.ProductService;
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
@RequestMapping("/product")
public class ProductController {
    @Autowired
    private ProductService productService;
    @GetMapping
    public ResponseEntity<Map<String,Object>> getAllProduct(){
        List<ProductRequest> productList =productService.getAllProduct();
        Map<String,Object>response = new HashMap<>();
        response.put("DT",productList);
        response.put("EC",0);
        response.put("EM", "Get all products succeed");
        return ResponseEntity.ok(response);
    }
    @GetMapping("/detail/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id){
        Optional<Product> product =productService.getProductById(id);
        return product.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
    @PostMapping("/add")
    public ResponseEntity<Map<String,Object>> createProduct(@RequestBody Product product){
        Product createdProduct = productService.createProduct(product);
        Map<String,Object>response = new HashMap<>();
        response.put("DT",createdProduct);
        response.put("EC",0);
        response.put("EM", "Product added successfully");

        return ResponseEntity.ok(response);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Map<String,Object>> updateProduct(@PathVariable Long id,@RequestBody Product product){
        Product updateProduct = productService.updateProduct(id,product);
        Map<String, Object> response = new HashMap<>();
        response.put("DT", updateProduct);
        response.put("EC", 0);
        response.put("EM", "Product updated successfully");

    return ResponseEntity.ok(response);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id){
        try{
            productService.deleteProduct(id);
            return ResponseEntity.status(HttpStatus.OK).body("Product deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting product");
        }
    }
}
