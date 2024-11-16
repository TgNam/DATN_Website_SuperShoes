package org.example.datn_website_supershoes.controller;

import org.example.datn_website_supershoes.dto.response.Response;
import org.example.datn_website_supershoes.repository.BillRepository;
import org.example.datn_website_supershoes.service.BillByEmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/billByEmployee")
public class BillRestApi {
    @Autowired
    private BillByEmployeeService billByEmployeeService;


    @GetMapping("/list-codeBill")
    private ResponseEntity<?> findListCodeBillWaitingForPayment() {
        try {
            return ResponseEntity.ok(billByEmployeeService.getDisplayAndWaitingBills());
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

    @GetMapping("/sortDisplayBills")
    private ResponseEntity<?> sortDisplayBills(
            @RequestParam(value = "displayBills", required = false) List<String> displayBills,
            @RequestParam(value = "selectills", required = false) List<String> selectills
    ) {
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
    private ResponseEntity<?> createBillByEmployee(@RequestParam(value = "displayBills", required = false) List<String> displayBills) {
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

    @GetMapping("/findBillRequestBycodeBill")
    private ResponseEntity<?> findBillResponseByCodeBill(@RequestParam(value = "codeBill", required = false) String codeBill) {
        try {
            if (codeBill == null) {
                return ResponseEntity.badRequest().body(
                        Response.builder()
                                .status(HttpStatus.BAD_REQUEST.toString())
                                .mess("Lỗi: ID mã hóa đơn không được để trống!")
                                .build()
                );
            }
            return ResponseEntity.ok(billByEmployeeService.findBillResponseByCodeBill(codeBill));
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
    @PostMapping("/payBillByEmployee")
    private ResponseEntity<?> payBillByEmployee (
            @RequestParam(value = "codeBill", required = false) String codeBill,//Mã hóa đơn
            @RequestParam(value = "delivery", required = false) boolean delivery,//Giao hàng
            @RequestParam(value = "postpaid", required = false) boolean postpaid,//Trả sau
            @RequestParam(value = "codeVoucher", required = false) String codeVoucher,//Mã phiếu giảm giá
            @RequestParam(value = "idAccount", required = false) Long idAccount,//id tài khoản mua hàng
            @RequestParam(value = "name", required = false) String name,//Tên người nhận hàng
            @RequestParam(value = "phoneNumber", required = false) String phoneNumber,//Số điện thoại người nhận hàng
            @RequestParam(value = "address", required = false) String address,
            @RequestParam(value = "note", required = false) String note//Thông tin cần lưu ý

    ){
        try {
            if (codeBill == null) {
                return ResponseEntity.badRequest().body(
                        Response.builder()
                                .status(HttpStatus.BAD_REQUEST.toString())
                                .mess("Lỗi: ID mã hóa đơn không được để trống!")
                                .build()
                );
            }
            billByEmployeeService.payBillByEmployee(codeBill,delivery,postpaid,codeVoucher,idAccount,name,phoneNumber,address,note);
            return ResponseEntity.ok("Thanh toán thành công");
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
