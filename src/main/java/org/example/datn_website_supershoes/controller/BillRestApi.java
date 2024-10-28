package org.example.datn_website_supershoes.controller;

import org.example.datn_website_supershoes.dto.response.Response;
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
    private ResponseEntity<?> findListCodeBillWaitingForPayment(){
    try{
        return ResponseEntity.ok(billByEmployeeService.getDisplayAndWaitingBills());
    }catch (RuntimeException e) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(Response.builder()
                        .status(HttpStatus.CONFLICT.toString())
                        .mess(e.getMessage())
                        .build()
                );
    }

    }
    @GetMapping("/sortDisplayBills")
    private ResponseEntity<?> sortDisplayBills(
            @RequestParam(value ="displayBills", required = false) List<String> displayBills,
            @RequestParam(value ="selectills", required = false) List<String> selectills
    ){
        try {
            return ResponseEntity
                    .ok(billByEmployeeService.sortDisplayBills(displayBills, selectills));
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
    @PostMapping("/create-billByEmployee")
    private ResponseEntity<?> createBillByEmployee(@RequestParam(value ="displayBills", required = false) List<String> displayBills){
        try {
            return ResponseEntity
                    .ok(billByEmployeeService.createBillByEmployee(displayBills));
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
