package org.example.datn_website_supershoes.controller;

import org.example.datn_website_supershoes.dto.request.CategoryRequest;
import org.example.datn_website_supershoes.dto.response.Response;
import org.example.datn_website_supershoes.dto.response.CategoryResponse;
import org.example.datn_website_supershoes.service.CategoryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/category")
public class CategoryRestAPI {
    @Autowired
    private CategoryService categoryService;
    @GetMapping("/list-category")
    private List<CategoryResponse> findByStatusActive(){
        return categoryService.findByStatus();
    }
    @GetMapping("/list-category-search")
    private List<CategoryResponse> findByStatusSearch(@RequestParam("search") String search){
        return categoryService.findByStatus().stream()
                .filter(CategoryResponse -> CategoryResponse.getName().toLowerCase().contains(search.trim().toLowerCase()))
                .collect(Collectors.toList());
    }
    @PutMapping("/update-status")
    private ResponseEntity<?> updateStatus(@RequestParam("id") Long id){
        try{
            if (categoryService.updateStatus(id)){
                return ResponseEntity.ok("OK");
            }
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("False");
        }catch (RuntimeException e){
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(Response.builder()
                            .status(HttpStatus.CONFLICT.toString())
                            .mess(e.getMessage())
                            .build()
                    );
        }
    }
    @PostMapping("/create-category")
    private ResponseEntity<?> createCategory(@RequestBody CategoryRequest categoryRequest){
        try {
            if (categoryService.createCategory(categoryRequest) != null) {
                return ResponseEntity.ok("Thêm Category thành công");
            }
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("Thêm thất bại");
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
}
