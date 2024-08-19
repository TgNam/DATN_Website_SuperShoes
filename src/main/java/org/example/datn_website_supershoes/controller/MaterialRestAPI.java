package org.example.datn_website_supershoes.controller;

import org.example.datn_website_supershoes.dto.request.MaterialRequest;
import org.example.datn_website_supershoes.dto.response.Response;
import org.example.datn_website_supershoes.dto.response.MaterialResponse;
import org.example.datn_website_supershoes.service.MaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/material")
public class MaterialRestAPI {
    @Autowired
    private MaterialService materialService;
    @GetMapping("/list-material")
    private List<MaterialResponse> findByStatusActive(){
        return materialService.findByStatus();
    }
    @GetMapping("/list-material-search")
    private List<MaterialResponse> findByStatusSearch(@RequestParam("search") String search){
        return materialService.findByStatus().stream()
                .filter(MaterialResponse -> MaterialResponse.getName().toLowerCase().contains(search.trim().toLowerCase()))
                .collect(Collectors.toList());
    }
    @PutMapping("/update-status")
    private ResponseEntity<?> updateStatus(@RequestParam("id") Long id){
        try{
            if (materialService.updateStatus(id)){
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
    @PostMapping("/create-material")
    private ResponseEntity<?> createMaterial(@RequestBody MaterialRequest materialRequest){
        try {
            if (materialService.createMaterial(materialRequest) != null) {
                return ResponseEntity.ok("Thêm chất liệu thành công");
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
