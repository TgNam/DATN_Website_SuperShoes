package org.example.datn_website_supershoes.controller;

import org.example.datn_website_supershoes.model.Voucher;
import org.example.datn_website_supershoes.service.VoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/voucher")
public class VoucherController {

    @Autowired
    private VoucherService voucherService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllVouchers() {
        List<Voucher> listVouchers = new ArrayList<>();
        listVouchers = voucherService.getAllVouchers();
        Map<String, Object> response = new HashMap<>();
        response.put("DT", listVouchers);
        response.put("EC", 0);
        response.put("EM", "GetAll list participants succeed");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<Voucher> getVoucherById(@PathVariable Long id) {
        Optional<Voucher> voucher = voucherService.getVoucherById(id);
        return voucher.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/add")
    public ResponseEntity<Map<String, Object>> createVoucher(@RequestBody Voucher newVoucher) {

        Voucher voucher = newVoucher;
        Voucher createdVoucher = voucherService.createVoucher(voucher);
        Map<String, Object> response = new HashMap<>();
        response.put("DT", createdVoucher);
        response.put("EC", 0);
        response.put("EM", "Create a new participant succeed");

        return ResponseEntity.ok(response);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Map<String, Object>> updateVoucher(@PathVariable Long id) {

        Voucher updatedVoucher = voucherService.updateVoucher(id);
        Map<String, Object> response = new HashMap<>();
        response.put("DT", updatedVoucher);
        response.put("EC", 0);
        response.put("EM", "Voucher update successful");
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteVoucher(@PathVariable Long id) {
        try {
            voucherService.deleteVoucher(id);
            return ResponseEntity.status(HttpStatus.OK).body("Voucher deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting voucher");
        }
    }
}
