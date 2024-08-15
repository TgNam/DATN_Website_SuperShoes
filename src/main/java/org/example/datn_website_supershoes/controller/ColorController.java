package org.example.datn_website_supershoes.controller;

import org.example.datn_website_supershoes.model.Color;
import org.example.datn_website_supershoes.model.Material;
import org.example.datn_website_supershoes.service.ColorService;
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
@RequestMapping("/color")
public class ColorController {
    @Autowired
    private ColorService colorService;
    @GetMapping
    public ResponseEntity<Map<String,Object>> getAllColor(){
        List<Color> ColorList =colorService.getAllColor();
        Map<String,Object>response = new HashMap<>();
        response.put("DT",ColorList);
        response.put("EC",0);
        response.put("EM", "Get all Color succeed");
        return ResponseEntity.ok(response);
    }
    @GetMapping("/detail/{id}")
    public ResponseEntity<Color> getColorById(@PathVariable Long id){
        Optional<Color> product =colorService.getColorById(id);
        return product.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
    @PostMapping("/add")
    public ResponseEntity<Map<String,Object>> createColor(@RequestBody Color color){
        Color createdColor = colorService.createColor(color);
        Map<String,Object>response = new HashMap<>();
        response.put("DT",createdColor);
        response.put("EC",0);
        response.put("EM", "Color added successfully");

        return ResponseEntity.ok(response);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Map<String,Object>> updateColor(@PathVariable Long id,@RequestBody Color color){
        Color updateColor = colorService.updateColor(id,color);
        Map<String, Object> response = new HashMap<>();
        response.put("DT", updateColor);
        response.put("EC", 0);
        response.put("EM", "Color updated successfully");

        return ResponseEntity.ok(response);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteColor(@PathVariable Long id){
        try{
            colorService.deleteColor(id);
            return ResponseEntity.status(HttpStatus.OK).body("Color deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting Color");
        }
    }
}
