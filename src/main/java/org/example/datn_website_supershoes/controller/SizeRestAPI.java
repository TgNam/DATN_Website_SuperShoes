package org.example.datn_website_supershoes.controller;

import org.example.datn_website_supershoes.Enum.Status;
import org.example.datn_website_supershoes.dto.request.SizeRequest;
import org.example.datn_website_supershoes.dto.response.Response;
import org.example.datn_website_supershoes.dto.response.SizeResponse;
import org.example.datn_website_supershoes.service.SizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/size")
public class SizeRestAPI {
    @Autowired
    private SizeService sizeService;
    @GetMapping("/list-size-active")
    private List<SizeResponse> findByStatusActive(){
        return sizeService.findByStatus();
    }
    @GetMapping("/list-size-search")
    private List<SizeResponse> findByStatusSearch(@RequestParam("search") String search){
        return sizeService.findByStatus().stream()
                .filter(sizeResponse -> sizeResponse.getName().toLowerCase().contains(search))
                .collect(Collectors.toList());
    }
    @PutMapping("/update-status")
    private ResponseEntity<?> updateStatus(@RequestParam("id") Long id){
        try{
            if (sizeService.updateStatus(id)){
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
    @PostMapping("/create-size")
    private ResponseEntity<?> createSize(@RequestBody SizeRequest sizeRequest){
        try {
            if (sizeService.createSize(sizeRequest) != null) {
                return ResponseEntity.ok("Thêm size thành công");
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
