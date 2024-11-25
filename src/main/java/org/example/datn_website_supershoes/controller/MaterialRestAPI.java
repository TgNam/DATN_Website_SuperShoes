package org.example.datn_website_supershoes.controller;

import jakarta.validation.Valid;
import org.example.datn_website_supershoes.dto.request.MaterialRequest;
import org.example.datn_website_supershoes.dto.response.Response;
import org.example.datn_website_supershoes.dto.response.MaterialResponse;
import org.example.datn_website_supershoes.model.Material;
import org.example.datn_website_supershoes.service.MaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/material")
public class MaterialRestAPI {
    @Autowired
    private MaterialService materialService;
    @GetMapping("/list-material")
    private List<MaterialResponse> findAllMaterial(){
        return materialService.findAllMaterial();
    }
    @GetMapping("/list-materialActive")
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
    private ResponseEntity<?> updateStatus(
            @RequestParam(value ="id", required = false) Long id,
            @RequestParam(value ="aBoolean", required = false) boolean aBoolean
    ){
        try{
            if (id == null) {
                return ResponseEntity.badRequest().body(
                        Response.builder()
                                .status(HttpStatus.BAD_REQUEST.toString())
                                .mess("Lỗi: ID chất liệu không được để trống!")
                                .build()
                );
            }
            Material material = materialService.updateStatus(id,aBoolean);
            return ResponseEntity.ok(material);
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
    private ResponseEntity<?> createMaterial(@RequestBody @Valid MaterialRequest materialRequest, BindingResult result){
        try {
            if (result.hasErrors()) {
                List<String> errors = result.getAllErrors().stream()
                        .map(error -> error.getDefaultMessage())
                        .collect(Collectors.toList());
                return ResponseEntity.badRequest().body(errors);
            }
            return ResponseEntity.ok(materialService.createMaterial(materialRequest));
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
