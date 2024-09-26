package org.example.datn_website_supershoes.controller;

import org.example.datn_website_supershoes.dto.request.SizeRequest;
import org.example.datn_website_supershoes.dto.response.Response;
import org.example.datn_website_supershoes.model.Bill;
import org.example.datn_website_supershoes.service.BillByEmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/billByEmployee")
public class BillRestApi {
    @Autowired
    private BillByEmployeeService billByEmployeeService;
    @GetMapping("/list-codeBill")
    private List<String> findListCodeBillWaitingForPayment(){
        return billByEmployeeService.getListCodeBillWaitingForPayment();
    }
    @PostMapping("/create-billByEmployee")
    private ResponseEntity<?> createSize(){
        try {
            Bill bill = billByEmployeeService.createBillByEmployee();
            if ( bill!= null) {
                return ResponseEntity.ok(bill.getCodeBill());
            }
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("Thêm hóa đơn thất bại");
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
