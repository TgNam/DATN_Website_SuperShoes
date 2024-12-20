package org.example.datn_website_supershoes.controller;

import jakarta.validation.Valid;
import org.example.datn_website_supershoes.dto.request.BrandRequest;
import org.example.datn_website_supershoes.dto.response.BrandResponse;
import org.example.datn_website_supershoes.dto.response.Response;
import org.example.datn_website_supershoes.model.Brand;
import org.example.datn_website_supershoes.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/brand")
public class BrandRestAPI {
    @Autowired
    BrandService brandService;
    @GetMapping("/list-brand")
    public ResponseEntity<?> findAllBrand(){
        try{
            return ResponseEntity.ok(brandService.findAllBrand());
        }catch (RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/list-brandActive")
    public ResponseEntity<?> findByStatusActive(){
        try{
            return ResponseEntity.ok(brandService.findByStatus());
        }catch (RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/list-brand-search")
    public ResponseEntity<?> findByStatusSearch(@RequestParam("search") String search){
        try{
            return ResponseEntity.ok(brandService.findAllBrand().stream()
                    .filter(brandResponse -> brandResponse.getName().toLowerCase().contains(search.trim().toLowerCase()))
                    .collect(Collectors.toList()));
        }catch (RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PutMapping("/update-status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateStatus(
            @RequestParam(value ="id", required = false) Long id,
            @RequestParam(value ="aBoolean", required = false) boolean aBoolean
    ){
        try{
            if (id == null) {
                return ResponseEntity.badRequest().body(
                        Response.builder()
                                .status(HttpStatus.BAD_REQUEST.toString())
                                .mess("Lỗi: ID hãng không được để trống!")
                                .build()
                );
            }
            Brand brand = brandService.updateStatus(id, aBoolean);
            return ResponseEntity.ok(brand);
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
    @PostMapping("/create-brand")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createSize(@RequestBody @Valid BrandRequest brandRequest, BindingResult result){
        try {
            if (result.hasErrors()) {
                List<String> errors = result.getAllErrors().stream()
                        .map(error -> error.getDefaultMessage())
                        .collect(Collectors.toList());
                return ResponseEntity.badRequest().body(errors);
            }
            return ResponseEntity.ok(brandService.createBrand(brandRequest));
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
