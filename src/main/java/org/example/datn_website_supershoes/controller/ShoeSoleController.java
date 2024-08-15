package org.example.datn_website_supershoes.controller;

import org.example.datn_website_supershoes.model.Material;
import org.example.datn_website_supershoes.model.ShoeSole;
import org.example.datn_website_supershoes.service.MaterialService;
import org.example.datn_website_supershoes.service.ShoeSoleService;
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
@RequestMapping("/shoeSole")
public class ShoeSoleController {

    @Autowired
    private ShoeSoleService shoeSoleService;
    @GetMapping
    public ResponseEntity<Map<String,Object>> getAllShoeSole(){
        List<ShoeSole> ShoeSoleList =shoeSoleService.getAllShoeSole();
        Map<String,Object>response = new HashMap<>();
        response.put("DT",ShoeSoleList);
        response.put("EC",0);
        response.put("EM", "Get all ShoeSole succeed");
        return ResponseEntity.ok(response);
    }
    @GetMapping("/detail/{id}")
    public ResponseEntity<ShoeSole> getShoeSoleById(@PathVariable Long id){
        Optional<ShoeSole> product =shoeSoleService.getShoeSoleById(id);
        return product.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
    @PostMapping("/add")
    public ResponseEntity<Map<String,Object>> createShoeSole(@RequestBody ShoeSole shoeSole){
        ShoeSole createdShoeSole= shoeSoleService.createShoeSole(shoeSole);
        Map<String,Object>response = new HashMap<>();
        response.put("DT",createdShoeSole);
        response.put("EC",0);
        response.put("EM", "ShoeSole added successfully");

        return ResponseEntity.ok(response);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Map<String,Object>> updateShoeSole(@PathVariable Long id,@RequestBody ShoeSole shoeSole){
        ShoeSole updateShoeSole = shoeSoleService.updateShoeSole(id,shoeSole);
        Map<String, Object> response = new HashMap<>();
        response.put("DT", updateShoeSole);
        response.put("EC", 0);
        response.put("EM", "ShoeSole updated successfully");

        return ResponseEntity.ok(response);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteShoeSole(@PathVariable Long id){
        try{
            shoeSoleService.deleteShoeSole(id);
            return ResponseEntity.status(HttpStatus.OK).body("ShoeSole deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting ShoeSole");
        }
    }
}
