package org.example.datn_website_supershoes.controller;

import org.example.datn_website_supershoes.model.Brand;
import org.example.datn_website_supershoes.model.Material;
import org.example.datn_website_supershoes.service.BrandService;
import org.example.datn_website_supershoes.service.MaterialService;
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
@RequestMapping("/brand")
public class BrandController {
    @Autowired
    private BrandService brandService;
    @GetMapping
    public ResponseEntity<Map<String,Object>> getAllBrand(){
        List<Brand> productList =brandService.getAllBrand();
        Map<String,Object>response = new HashMap<>();
        response.put("DT",productList);
        response.put("EC",0);
        response.put("EM", "Get all Brand succeed");
        return ResponseEntity.ok(response);
    }
    @GetMapping("/detail/{id}")
    public ResponseEntity<Brand> getProductById(@PathVariable Long id){
        Optional<Brand> product =brandService.getBrandById(id);
        return product.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
    @PostMapping("/add")
    public ResponseEntity<Map<String,Object>> createBrand(@RequestBody Brand brand){
        Brand createdBrand = brandService.createBrand(brand);
        Map<String,Object>response = new HashMap<>();
        response.put("DT",createdBrand);
        response.put("EC",0);
        response.put("EM", "Brand added successfully");

        return ResponseEntity.ok(response);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Map<String,Object>> updateBrand(@PathVariable Long id,@RequestBody Brand brand){
        Brand updateBrand = brandService.updateBrand(id,brand);
        Map<String, Object> response = new HashMap<>();
        response.put("DT", updateBrand);
        response.put("EC", 0);
        response.put("EM", "Brand updated successfully");

        return ResponseEntity.ok(response);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteBrand(@PathVariable Long id){
        try{
            brandService.deletebrand(id);
            return ResponseEntity.status(HttpStatus.OK).body("Brand deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting Brand");
        }
    }
}
