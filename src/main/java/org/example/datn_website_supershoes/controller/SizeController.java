package org.example.datn_website_supershoes.controller;

import org.example.datn_website_supershoes.model.Material;
import org.example.datn_website_supershoes.model.Size;
import org.example.datn_website_supershoes.service.MaterialService;
import org.example.datn_website_supershoes.service.SizeService;
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
@RequestMapping("/size")
public class SizeController {
    @Autowired
    private SizeService sizeService;
    @GetMapping
    public ResponseEntity<Map<String,Object>> getAllSize(){
        List<Size> productList =sizeService.getAllSize();
        Map<String,Object>response = new HashMap<>();
        response.put("DT",productList);
        response.put("EC",0);
        response.put("EM", "Get all Size succeed");
        return ResponseEntity.ok(response);
    }
    @GetMapping("/detail/{id}")
    public ResponseEntity<Size> getSizeById(@PathVariable Long id){
        Optional<Size> product =sizeService.getSizeById(id);
        return product.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
    @PostMapping("/add")
    public ResponseEntity<Map<String,Object>> createSize(@RequestBody Size material){
        Size createdSize = sizeService.createSize(material);
        Map<String,Object>response = new HashMap<>();
        response.put("DT",createdSize);
        response.put("EC",0);
        response.put("EM", "Size added successfully");

        return ResponseEntity.ok(response);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Map<String,Object>> updateSize(@PathVariable Long id,@RequestBody Size material){
        Size updateSize = sizeService.updateSize(id,material);
        Map<String, Object> response = new HashMap<>();
        response.put("DT", updateSize);
        response.put("EC", 0);
        response.put("EM", "Size updated successfully");

        return ResponseEntity.ok(response);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteSize(@PathVariable Long id){
        try{
            sizeService.deleteSize(id);
            return ResponseEntity.status(HttpStatus.OK).body("Size deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting Size");
        }
    }
}
