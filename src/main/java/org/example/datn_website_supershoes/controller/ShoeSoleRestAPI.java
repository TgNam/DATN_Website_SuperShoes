package org.example.datn_website_supershoes.controller;

import jakarta.validation.Valid;
import org.example.datn_website_supershoes.dto.request.ShoeSoleRequest;
import org.example.datn_website_supershoes.dto.response.Response;
import org.example.datn_website_supershoes.dto.response.ShoeSoleResponse;
import org.example.datn_website_supershoes.model.ShoeSole;
import org.example.datn_website_supershoes.service.ShoeSoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/shoeSole")
public class ShoeSoleRestAPI {

    @Autowired
    ShoeSoleService shoeSoleService;

    @GetMapping("/list-shoeSole")
    public ResponseEntity<?> findAllShoeSole() {
        try{
            return ResponseEntity.ok(shoeSoleService.findAllShoeSole());
        }catch (RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/list-shoeSoleActive")
    public ResponseEntity<?> findByStatusActive() {
        try{
            return ResponseEntity.ok(shoeSoleService.findByStatus());
        }catch (RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/list-shoeSole-search")
    public ResponseEntity<?> findByStatusSearch(@RequestParam("search") String search) {
        try{
            return ResponseEntity.ok(shoeSoleService.findByStatus().stream()
                    .filter(ShoeSoleResponse -> ShoeSoleResponse.getName().toLowerCase().contains(search.trim().toLowerCase()))
                    .collect(Collectors.toList()));
        }catch (RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/update-status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateStatus(
            @RequestParam(value = "id", required = false) Long id,
            @RequestParam(value = "aBoolean", required = false) boolean aBoolean) {
        try {
            if (id == null) {
                return ResponseEntity.badRequest().body(
                        Response.builder()
                                .status(HttpStatus.BAD_REQUEST.toString())
                                .mess("Lỗi: ID loại đế giày không được để trống!")
                                .build()
                );
            }
            ShoeSole shoeSole = shoeSoleService.updateStatus(id, aBoolean);
            return ResponseEntity.ok(shoeSole);
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

    @PostMapping("/create-shoeSole")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createShoeSole(@RequestBody @Valid ShoeSoleRequest shoeSoleRequest, BindingResult result) {
        try {
            if (result.hasErrors()) {
                List<String> errors = result.getAllErrors().stream()
                        .map(error -> error.getDefaultMessage())
                        .collect(Collectors.toList());
                return ResponseEntity.badRequest().body(errors);
            }
            System.out.println(shoeSoleRequest);
            return ResponseEntity.ok(shoeSoleService.createShoeSole(shoeSoleRequest));
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
