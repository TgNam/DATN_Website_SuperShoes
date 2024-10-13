package org.example.datn_website_supershoes.controller;

import org.example.datn_website_supershoes.dto.request.ColorRequest;
import org.example.datn_website_supershoes.dto.response.Response;
import org.example.datn_website_supershoes.dto.response.ColorResponse;
import org.example.datn_website_supershoes.service.ColorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/color")
public class ColorRestAPI {
    @Autowired
    private ColorService colorService;
    @GetMapping("/list-color")
    private List<ColorResponse> findAllColor(){
        return colorService.findAllColor();
    }
    @GetMapping("/listColorACTIVE")
    private List<ColorResponse> findByStatusActive(){
        return colorService.findColorByStatusACTIVE();
    }
    @GetMapping("/list-color-search")
    private List<ColorResponse> findByStatusSearch(@RequestParam("search") String search){
        return colorService.findAllColor().stream()
                .filter(ColorResponse -> ColorResponse.getName().toLowerCase().contains(search.trim().toLowerCase()))
                .collect(Collectors.toList());
    }
    @PutMapping("/update-status")
    private ResponseEntity<?> updateStatus(@RequestParam("id") Long id){
        try{
            if (colorService.updateStatus(id)){
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
    @PostMapping("/create-color")
    private ResponseEntity<?> createColor(@RequestBody ColorRequest ColorRequest){
        try {
            if (colorService.createColor(ColorRequest) != null) {
                return ResponseEntity.ok("Thêm màu "+ColorRequest.getCodeColor()+" thành công");
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
