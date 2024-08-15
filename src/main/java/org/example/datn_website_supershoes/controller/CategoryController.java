package org.example.datn_website_supershoes.controller;

import org.example.datn_website_supershoes.model.Category;

import org.example.datn_website_supershoes.service.CategoryService;

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
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    @GetMapping
    public ResponseEntity<Map<String,Object>> getAllCategory(){
        List<Category> productList =categoryService.getAllCategory();
        Map<String,Object>response = new HashMap<>();
        response.put("DT",productList);
        response.put("EC",0);
        response.put("EM", "Get all Category succeed");
        return ResponseEntity.ok(response);
    }
    @GetMapping("/detail/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable Long id){
        Optional<Category> product =categoryService.getCategoryById(id);
        return product.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
    @PostMapping("/add")
    public ResponseEntity<Map<String,Object>> createCategory(@RequestBody Category category){
        Category createdCategory = categoryService.createCategory(category);
        Map<String,Object>response = new HashMap<>();
        response.put("DT",createdCategory);
        response.put("EC",0);
        response.put("EM", "Category added successfully");

        return ResponseEntity.ok(response);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Map<String,Object>> updateCategory(@PathVariable Long id,@RequestBody Category category){
        Category updateCategory = categoryService.updateCategory(id,category);
        Map<String, Object> response = new HashMap<>();
        response.put("DT", updateCategory);
        response.put("EC", 0);
        response.put("EM", "Category updated successfully");

        return ResponseEntity.ok(response);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long id){
        try{
            categoryService.deleteCategory(id);
            return ResponseEntity.status(HttpStatus.OK).body("Category deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting Category");
        }
    }
}
