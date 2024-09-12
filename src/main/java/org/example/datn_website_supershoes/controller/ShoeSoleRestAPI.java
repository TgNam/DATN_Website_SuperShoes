package org.example.datn_website_supershoes.controller;

import org.example.datn_website_supershoes.dto.request.ShoeSoleRequest;
import org.example.datn_website_supershoes.dto.response.Response;
import org.example.datn_website_supershoes.dto.response.ShoeSoleResponse;
import org.example.datn_website_supershoes.dto.response.ShoeSoleResponse;
import org.example.datn_website_supershoes.service.ShoeSoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/shoeSole")
public class ShoeSoleRestAPI {

    @Autowired
    private ShoeSoleService shoeSoleService;
    @GetMapping("/list-shoeSole")
    private List<ShoeSoleResponse> findByStatusActive(){
        return shoeSoleService.findByStatus();
    }
    @GetMapping("/list-shoeSole-search")
    private List<ShoeSoleResponse> findByStatusSearch(@RequestParam("search") String search){
        return shoeSoleService.findByStatus().stream()
                .filter(ShoeSoleResponse -> ShoeSoleResponse.getName().toLowerCase().contains(search.trim().toLowerCase()))
                .collect(Collectors.toList());
    }
    @PutMapping("/update-status")
    private ResponseEntity<?> updateStatus(@RequestParam("id") Long id){
        try{
            if (shoeSoleService.updateStatus(id)){
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
    @PostMapping("/create-shoeSole")
    private ResponseEntity<?> createShoeSole(@RequestBody ShoeSoleRequest shoeSoleRequest){
        try {
            if (shoeSoleService.createShoeSole(shoeSoleRequest) != null) {
                return ResponseEntity.ok("Thêm loại đế thành công");
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
