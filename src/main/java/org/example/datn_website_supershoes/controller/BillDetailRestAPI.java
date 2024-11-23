package org.example.datn_website_supershoes.controller;

import jakarta.validation.Valid;
import org.example.datn_website_supershoes.dto.request.ProductDetailPromoRequest;
import org.example.datn_website_supershoes.dto.response.BillDetailOrderResponse;
import org.example.datn_website_supershoes.dto.response.Response;
import org.example.datn_website_supershoes.service.BillDetailByEmployeeService;
import org.example.datn_website_supershoes.service.BillDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/billDetailByEmployee")
public class BillDetailRestAPI {
    @Autowired
    private BillDetailService billDetailService;
    @Autowired
    private BillDetailByEmployeeService billDetailByEmployeeService;
    @PostMapping("/createBillDetailByEmployee")
    private ResponseEntity<?> createBillDetailByEmployee(
            @RequestParam(value ="codeBill", required = false) String codeBill,
            @RequestBody @Valid List<ProductDetailPromoRequest> productDetail, BindingResult result
    ){
        try {
            if (codeBill == null) {
                return ResponseEntity.badRequest().body(
                        Response.builder()
                                .status(HttpStatus.BAD_REQUEST.toString())
                                .mess("Lỗi: ID hóa đơn không được để trống!")
                                .build()
                );
            }
            if (result.hasErrors()) {
                List<String> errors = result.getAllErrors().stream()
                        .map(error -> error.getDefaultMessage())
                        .collect(Collectors.toList());
                return ResponseEntity.badRequest().body(errors);
            }
            billDetailService.createBillDetailByIdBill(codeBill,productDetail);
            return ResponseEntity
                    .ok("Thêm sản phẩm vào giỏ hàng thành công!");
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
    @PostMapping("/updateBillAndCreateBillDetailByIdBill")
    private ResponseEntity<?> updateBillAndCreateBillDetailByIdBill(
            @RequestParam(value ="codeBill", required = false) String codeBill,
            @RequestBody @Valid List<ProductDetailPromoRequest> productDetail, BindingResult result
    ){
        try {
            if (codeBill == null) {
                return ResponseEntity.badRequest().body(
                        Response.builder()
                                .status(HttpStatus.BAD_REQUEST.toString())
                                .mess("Lỗi: ID hóa đơn không được để trống!")
                                .build()
                );
            }
            if (result.hasErrors()) {
                List<String> errors = result.getAllErrors().stream()
                        .map(error -> error.getDefaultMessage())
                        .collect(Collectors.toList());
                return ResponseEntity.badRequest().body(errors);
            }
            billDetailService.updateBillAndCreateBillDetailByIdBill(codeBill,productDetail);
            return ResponseEntity
                    .ok("Thêm sản phẩm vào giỏ hàng thành công!");
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
    @GetMapping("/detail")
    public ResponseEntity<?> getBillByCodeBill(@RequestParam(value ="codeBill", required = false) String codeBill) {
        if (codeBill==null){
            return ResponseEntity.badRequest().body(
                    Response.builder()
                            .status(HttpStatus.BAD_REQUEST.toString())
                            .mess("Lỗi: Mã hóa đơn không được để trống!")
                            .build()
            );
        }
        List<BillDetailOrderResponse> billDetailOrderResponses = billDetailByEmployeeService.getBillDetailsByCodeBill(codeBill);
        return ResponseEntity.ok(billDetailOrderResponses);
    }
}
