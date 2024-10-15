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
@RequestMapping("api/size")
public class SizeRestAPI {

    @Autowired
    private SizeService sizeService;

    @GetMapping("/list-size")
    private List<SizeResponse> findAllSize(){
        return sizeService.findAllSize();
    }
    @GetMapping("/listSizeACTIVE")
    private List<SizeResponse> findByStatusActive(){
        return sizeService.findSizeByStatusACTIVE();
    }
    @GetMapping("/list-size-search")
    private List<SizeResponse> findByStatusSearch(@RequestParam("search") String search){
        return sizeService.findAllSize().stream()
                .filter(sizeResponse -> sizeResponse.getName().toLowerCase().contains(search.trim().toLowerCase()))
                .collect(Collectors.toList());
    }

    @PutMapping("/update-status")
    private ResponseEntity<?> updateStatus(
            @RequestParam(value ="id", required = false) Long id,
            @RequestParam(value ="status", required = false) String status
    ){
        try{
            if (id == null) {
                return ResponseEntity.badRequest().body(
                        Response.builder()
                                .status(HttpStatus.BAD_REQUEST.toString())
                                .mess("Lỗi: ID kích cỡ không được để trống!")
                                .build()
                );
            }
            if (status==null){
                return ResponseEntity.badRequest().body(
                        Response.builder()
                                .status(HttpStatus.BAD_REQUEST.toString())
                                .mess("Lỗi: Trạng thái không được để trống!")
                                .build()
                );
            }
            Size size  = sizeService.updateStatus(id,status);
            return ResponseEntity.ok(size);
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
    private ResponseEntity<?> createSize(@RequestBody @Valid SizeRequest sizeRequest, BindingResult result) {
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
