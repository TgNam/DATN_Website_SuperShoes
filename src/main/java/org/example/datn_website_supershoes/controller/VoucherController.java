package org.example.datn_website_supershoes.controller;

import jakarta.persistence.criteria.Predicate;
import jakarta.validation.constraints.NotNull;
import org.example.datn_website_supershoes.dto.request.VoucherRequest;
import org.example.datn_website_supershoes.dto.response.Response;
import org.example.datn_website_supershoes.dto.response.VoucherResponse;
import org.example.datn_website_supershoes.model.Voucher;
import org.example.datn_website_supershoes.service.VoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/voucher")
public class VoucherController {

    @Autowired
    private VoucherService voucherService;

    @GetMapping("/list-voucher")
    public Page<VoucherResponse> getAllVouchers(
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "codeVoucher", required = false) String codeVoucher,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        Specification<Voucher> spec = (root, query, criteriaBuilder) -> {
            Predicate p = criteriaBuilder.conjunction();
            if (status != null && !status.isEmpty()) {
                p = criteriaBuilder.and(p, criteriaBuilder.equal(root.get("status"), status));
            }
            if (codeVoucher != null && !codeVoucher.isEmpty()) {
                p = criteriaBuilder.and(p, criteriaBuilder.like(root.get("codeVoucher"), "%" + codeVoucher + "%"));
            }
            return p;
        };

        Pageable pageable = PageRequest.of(page, size);
        return voucherService.getVouchers(spec, pageable);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createVoucher(@RequestBody @NotNull VoucherRequest voucherRequest) {
        try {
            return ResponseEntity.ok(voucherService.createVoucher(voucherRequest));
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

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateVoucher(@PathVariable("id") long id,
                                           @RequestBody VoucherRequest voucherRequest) {
        try {
            Voucher updatedVoucher = voucherService.updateVoucher(id, voucherRequest);
            return ResponseEntity.ok(updatedVoucher);
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Response.builder()
                            .status(HttpStatus.NOT_FOUND.toString())
                            .mess(e.getMessage())
                            .build()
                    );
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteVoucher(@PathVariable("id") Long id) {
        try {
            voucherService.deleteVoucher(id);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(Response.builder()
                            .status(HttpStatus.OK.toString())
                            .mess("Delete success")
                            .build()
                    );
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Response.builder()
                            .status(HttpStatus.NOT_FOUND.toString())
                            .mess(e.getMessage())
                            .build()
                    );
        }
    }

}
