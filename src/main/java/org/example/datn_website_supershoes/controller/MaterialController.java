package org.example.datn_website_supershoes.controller;

import org.example.datn_website_supershoes.model.Material;
import org.example.datn_website_supershoes.model.Product;
import org.example.datn_website_supershoes.service.MaterialService;
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
@RequestMapping("/material")
public class MaterialController {
    @Autowired
    private MaterialService materialService;
    @GetMapping
    public ResponseEntity<Map<String,Object>> getAllProduct(){
        List<Material> productList =materialService.getAllMaterial();
        Map<String,Object>response = new HashMap<>();
        response.put("DT",productList);
        response.put("EC",0);
        response.put("EM", "Get all Material succeed");
        return ResponseEntity.ok(response);
    }
    @GetMapping("/detail/{id}")
    public ResponseEntity<Material> getProductById(@PathVariable Long id){
        Optional<Material> product =materialService.getMaterialById(id);
        return product.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
    @PostMapping("/add")
    public ResponseEntity<Map<String,Object>> createProduct(@RequestBody Material material){
        Material createdMaterial = materialService.createMaterial(material);
        Map<String,Object>response = new HashMap<>();
        response.put("DT",createdMaterial);
        response.put("EC",0);
        response.put("EM", "Material added successfully");

        return ResponseEntity.ok(response);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Map<String,Object>> updateProduct(@PathVariable Long id,@RequestBody Material material){
        Material updateMaterial = materialService.updateMaterial(id,material);
        Map<String, Object> response = new HashMap<>();
        response.put("DT", updateMaterial);
        response.put("EC", 0);
        response.put("EM", "Material updated successfully");

        return ResponseEntity.ok(response);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id){
        try{
            materialService.deleteMaterial(id);
            return ResponseEntity.status(HttpStatus.OK).body("Material deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting Material");
        }
    }
}
