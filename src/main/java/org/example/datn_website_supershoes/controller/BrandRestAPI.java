package org.example.datn_website_supershoes.controller;

import org.example.datn_website_supershoes.dto.request.BrandRequest;
import org.example.datn_website_supershoes.dto.request.SizeRequest;
import org.example.datn_website_supershoes.dto.response.BrandResponse;
import org.example.datn_website_supershoes.dto.response.Response;
import org.example.datn_website_supershoes.dto.response.SizeResponse;
import org.example.datn_website_supershoes.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/brand")
public class BrandRestAPI {
    @Autowired
    private BrandService brandService;
    @GetMapping("/list-brand")
    private List<BrandResponse> findByStatusActive(){
        return brandService.findByStatus();
    }
    @GetMapping("/list-brand-search")
    private List<BrandResponse> findByStatusSearch(@RequestParam("search") String search){
        return brandService.findByStatus().stream()
                .filter(brandResponse -> brandResponse.getName().toLowerCase().contains(search.trim().toLowerCase()))
                .collect(Collectors.toList());
    }
    @PutMapping("/update-status")
    private ResponseEntity<?> updateStatus(@RequestParam("id") Long id){
        try{
            if (brandService.updateStatus(id)){
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
    @PostMapping("/create-brand")
    private ResponseEntity<?> createSize(@RequestBody BrandRequest brandRequest){
        try {
            if (brandService.createBrand(brandRequest) != null) {
                return ResponseEntity.ok("Thêm hãng thành công");
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
