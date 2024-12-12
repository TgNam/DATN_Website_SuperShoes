package org.example.datn_website_supershoes.controller;

import jakarta.validation.Valid;
import org.example.datn_website_supershoes.dto.request.ProductDetailPromoRequest;
import org.example.datn_website_supershoes.dto.response.Response;
import org.example.datn_website_supershoes.repository.BillRepository;
import org.example.datn_website_supershoes.service.BillByEmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/billByEmployee")
public class BillRestApi {
    @Autowired
    BillByEmployeeService billByEmployeeService;


    @GetMapping("/list-codeBill")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    public ResponseEntity<?> findListCodeBillWaitingForPayment() {
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
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    public ResponseEntity<?> sortDisplayBills(
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
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    public ResponseEntity<?> createBillByEmployee(@RequestParam(value = "displayBills", required = false) List<String> displayBills) {
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
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    public ResponseEntity<?> findBillResponseByCodeBill(@RequestParam(value = "codeBill", required = false) String codeBill) {
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
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    public ResponseEntity<?> payBillByEmployee(
            @RequestParam(value = "codeBill", required = false) String codeBill,//Mã hóa đơn
            @RequestParam(value = "delivery", required = false) boolean delivery,//Giao hàng
            @RequestParam(value = "postpaid", required = false) boolean postpaid,//Trả sau
            @RequestParam(value = "codeVoucher", required = false) String codeVoucher,//Mã phiếu giảm giá
            @RequestParam(value = "idAccount", required = false) Long idAccount,//id tài khoản mua hàng
            @RequestParam(value = "name", required = false) String name,//Tên người nhận hàng
            @RequestParam(value = "phoneNumber", required = false) String phoneNumber,//Số điện thoại người nhận hàng
            @RequestParam(value = "address", required = false) String address,
            @RequestParam(value = "note", required = false) String note//Thông tin cần lưu ý

    ) {
        try {
            if (codeBill == null) {
                return ResponseEntity.badRequest().body(
                        Response.builder()
                                .status(HttpStatus.BAD_REQUEST.toString())
                                .mess("Lỗi: ID mã hóa đơn không được để trống!")
                                .build()
                );
            }
            billByEmployeeService.payBillByEmployee(codeBill, delivery, postpaid, codeVoucher, idAccount, name, phoneNumber, address, note);
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

    @PostMapping("/payBillOnline")
    public ResponseEntity<?> payBillOnline(
            @RequestParam(value = "IdCartDetail", required = false) List<Long> IdCartDetail,
            @RequestParam(value = "codeVoucher", required = false) String codeVoucher,//Mã phiếu giảm giá
            @RequestParam(value = "idAccount", required = false) Long idAccount,//id tài khoản mua hàng
            @RequestParam(value = "name", required = false) String name,//Tên người nhận hàng
            @RequestParam(value = "phoneNumber", required = false) String phoneNumber,//Số điện thoại người nhận hàng
            @RequestParam(value = "address", required = false) String address,
            @RequestParam(value = "note", required = false) String note//Thông tin cần lưu ý

    ) {
        try {
            if (IdCartDetail == null) {
                return ResponseEntity.badRequest().body(
                        Response.builder()
                                .status(HttpStatus.BAD_REQUEST.toString())
                                .mess("Lỗi:Không có sản phẩm để thanh toán!")
                                .build()
                );
            }
            if (idAccount == null) {
                return ResponseEntity.badRequest().body(
                        Response.builder()
                                .status(HttpStatus.BAD_REQUEST.toString())
                                .mess("Lỗi: Vui lòng đăng nhập lại giỏ hàng!")
                                .build()
                );
            }
            if (name == null) {
                return ResponseEntity.badRequest().body(
                        Response.builder()
                                .status(HttpStatus.BAD_REQUEST.toString())
                                .mess("Lỗi: Tên của người nhận hàng không được để trống!")
                                .build()
                );
            }
            if (phoneNumber == null) {
                return ResponseEntity.badRequest().body(
                        Response.builder()
                                .status(HttpStatus.BAD_REQUEST.toString())
                                .mess("Lỗi: Số điện thoại của người nhận hàng không được để trống!")
                                .build()
                );
            }
            if (address == null) {
                return ResponseEntity.badRequest().body(
                        Response.builder()
                                .status(HttpStatus.BAD_REQUEST.toString())
                                .mess("Lỗi: Địa chỉ của người nhận hàng không được để trống!")
                                .build()
                );
            }
            billByEmployeeService.payBillOnline(IdCartDetail, codeVoucher, idAccount, name, phoneNumber, address, note);
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
    @PostMapping("/payBillOnlinev2")
    public ResponseEntity<?> payBillOnlinev2(
            @RequestParam(value = "codeVoucher", required = false) String codeVoucher,//Mã phiếu giảm giá
            @RequestParam(value = "idAccount", required = false) Long idAccount,//id tài khoản mua hàng
            @RequestParam(value = "name", required = false) String name,//Tên người nhận hàng
            @RequestParam(value = "phoneNumber", required = false) String phoneNumber,//Số điện thoại người nhận hàng
            @RequestParam(value = "address", required = false) String address,
            @RequestParam(value = "note", required = false) String note,//Thông tin cần lưu ý
            @RequestBody List<@Valid ProductDetailPromoRequest> productDetailRequest,
            BindingResult result
    ) {
        try {
            if (productDetailRequest == null || productDetailRequest.isEmpty()) {
                return ResponseEntity.badRequest().body(
                        Response.builder()
                                .status(HttpStatus.BAD_REQUEST.toString())
                                .mess("Danh sách sản phẩm không được để trống!")
                                .build()
                );
            }
            if (result.hasErrors()) {
                List<String> errors = result.getAllErrors().stream()
                        .map(error -> error.getDefaultMessage())
                        .collect(Collectors.toList());
                return ResponseEntity.badRequest().body(errors);
            }
            if (idAccount == null) {
                return ResponseEntity.badRequest().body(
                        Response.builder()
                                .status(HttpStatus.BAD_REQUEST.toString())
                                .mess("Lỗi: Vui lòng đăng nhập lại giỏ hàng!")
                                .build()
                );
            }
            if (name == null) {
                return ResponseEntity.badRequest().body(
                        Response.builder()
                                .status(HttpStatus.BAD_REQUEST.toString())
                                .mess("Lỗi: Tên của người nhận hàng không được để trống!")
                                .build()
                );
            }
            if (phoneNumber == null) {
                return ResponseEntity.badRequest().body(
                        Response.builder()
                                .status(HttpStatus.BAD_REQUEST.toString())
                                .mess("Lỗi: Số điện thoại của người nhận hàng không được để trống!")
                                .build()
                );
            }
            if (address == null) {
                return ResponseEntity.badRequest().body(
                        Response.builder()
                                .status(HttpStatus.BAD_REQUEST.toString())
                                .mess("Lỗi: Địa chỉ của người nhận hàng không được để trống!")
                                .build()
                );
            }
            billByEmployeeService.payBillOnlinev2(productDetailRequest, codeVoucher, idAccount, name, phoneNumber, address, note);
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
