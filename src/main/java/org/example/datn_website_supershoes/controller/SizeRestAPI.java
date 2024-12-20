package org.example.datn_website_supershoes.controller;

import jakarta.validation.Valid;
import org.example.datn_website_supershoes.dto.request.SizeRequest;
import org.example.datn_website_supershoes.dto.response.Response;
import org.example.datn_website_supershoes.dto.response.SizeResponse;
import org.example.datn_website_supershoes.model.Size;
import org.example.datn_website_supershoes.service.SizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/size")
public class SizeRestAPI {

    @Autowired
    SizeService sizeService;

    @GetMapping("/list-size")
    public ResponseEntity<?> findAllSize() {
        try{
            return ResponseEntity.ok(sizeService.findAllSize());
        }catch (RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/listSizeACTIVE")
    public ResponseEntity<?> findByStatusActive() {
        try{
            return ResponseEntity.ok(sizeService.findSizeByStatusACTIVE());
        }catch (RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/list-size-search")
    public ResponseEntity<?> findByStatusSearch(@RequestParam("search") String search) {
        try{
            return ResponseEntity.ok(sizeService.findAllSize().stream()
                    .filter(sizeResponse -> sizeResponse.getName().toLowerCase().contains(search.trim().toLowerCase()))
                    .collect(Collectors.toList()));
        }catch (RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/update-status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateStatus(
            @RequestParam(value = "id", required = false) Long id,
            @RequestParam(value = "aBoolean", required = false) boolean aBoolean
    ) {
        try {
            if (id == null) {
                return ResponseEntity.badRequest().body(
                        Response.builder()
                                .status(HttpStatus.BAD_REQUEST.toString())
                                .mess("Lỗi: ID kích cỡ không được để trống!")
                                .build()
                );
            }
            Size size = sizeService.updateStatus(id, aBoolean);
            return ResponseEntity.ok(size);
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

    @PostMapping("/create-size")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createSize(@RequestBody @Valid SizeRequest sizeRequest, BindingResult result) {
        try {
            if (result.hasErrors()) {
                List<String> errors = result.getAllErrors().stream()
                        .map(error -> error.getDefaultMessage())
                        .collect(Collectors.toList());
                return ResponseEntity.badRequest().body(errors);
            }
            return ResponseEntity.ok(sizeService.createSize(sizeRequest));
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
