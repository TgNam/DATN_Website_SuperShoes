package org.example.datn_website_supershoes.controller;

import jakarta.persistence.criteria.Predicate;
import jakarta.validation.constraints.NotNull;
import org.example.datn_website_supershoes.dto.request.VoucherRequest;
import org.example.datn_website_supershoes.dto.response.Response;
import org.example.datn_website_supershoes.dto.response.VoucherResponse;
import org.example.datn_website_supershoes.model.Voucher;
import org.example.datn_website_supershoes.service.VoucherService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/voucher")
public class VoucherController {

    @Autowired
    private VoucherService voucherService;

    @GetMapping("/list-voucher")
    public ResponseEntity<Map<String, Object>> getAllVouchers(
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "searchTerm", required = false) String searchTerm,
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate endDate,
            @RequestParam(value = "type", required = false) Integer type,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        String trimmedSearchTerm = Optional.ofNullable(searchTerm)
                .map(String::trim)
                .filter(term -> !term.isEmpty())
                .orElse(null);

        Specification<Voucher> spec = (root, query, criteriaBuilder) -> {
            Predicate p = criteriaBuilder.conjunction();

            if (status != null && !status.isEmpty()) {
                p = criteriaBuilder.and(p, criteriaBuilder.equal(root.get("status"), status));
            }

            if (trimmedSearchTerm != null) {
                String likePattern = "%" + trimmedSearchTerm.toLowerCase() + "%";
                Predicate codePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("codeVoucher")), likePattern);
                Predicate namePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), likePattern);
                p = criteriaBuilder.and(p, criteriaBuilder.or(codePredicate, namePredicate));
            }

            if (type != null) {
                p = criteriaBuilder.and(p, criteriaBuilder.equal(root.get("type"), type));
            }

            if (startDate != null) {
                Predicate startAtPredicate = criteriaBuilder.greaterThanOrEqualTo(root.get("startAt"), startDate.atStartOfDay());
                p = criteriaBuilder.and(p, startAtPredicate);
            }

            if (endDate != null) {
                Predicate endAtPredicate = criteriaBuilder.lessThanOrEqualTo(root.get("endAt"), endDate.atTime(23, 59, 59));
                p = criteriaBuilder.and(p, endAtPredicate);
            }

            return p;
        };

        Pageable pageable = PageRequest.of(page, size);
        Page<VoucherResponse> vouchers = voucherService.getVouchers(spec, pageable);
        long totalRecords = voucherService.countVouchers(spec);

        Map<String, Object> response = new HashMap<>();
        response.put("vouchers", vouchers.getContent());
        response.put("currentPage", vouchers.getNumber());
        response.put("totalItems", vouchers.getTotalElements());
        response.put("totalPages", vouchers.getTotalPages());
        response.put("totalRecords", totalRecords);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createVoucher(@RequestBody @NotNull VoucherRequest voucherRequest,
                                           @RequestParam(required = false) Long userId) {
        try {
            Long creatorId = Optional.ofNullable(userId).orElse(1L);  // Default to user ID 1
            Voucher createdVoucher = voucherService.createVoucher(voucherRequest, creatorId);
            VoucherResponse response = convertToVoucherResponse(createdVoucher);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Response.builder()
                            .status(HttpStatus.CONFLICT.toString())
                            .mess(e.getMessage())
                            .build());
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateVoucher(@PathVariable("id") long id,
                                           @RequestBody VoucherRequest voucherRequest,
                                           @RequestParam(required = false) Long userId) {
        try {
            Long updaterId = Optional.ofNullable(userId).orElse(1L);
            Voucher updatedVoucher = voucherService.updateVoucher(id, voucherRequest, updaterId);
            VoucherResponse response = convertToVoucherResponse(updatedVoucher);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Response.builder()
                            .status(HttpStatus.NOT_FOUND.toString())
                            .mess(e.getMessage())
                            .build());
        }
    }


    @GetMapping("/detail/{id}")
    public ResponseEntity<?> getVoucher(@PathVariable Long id) {
        try {
            VoucherResponse voucherResponse = voucherService.getVoucherById(id);
            if ("EXPIRED".equals(voucherResponse.getStatus())) {
                throw new RuntimeException("Cannot view voucher that has expired.");
            }
            return ResponseEntity.ok(voucherResponse);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Response.builder()
                            .status(HttpStatus.BAD_REQUEST.toString())
                            .mess(e.getMessage())
                            .build());
        }
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<?> deleteVoucher(@PathVariable("id") Long id) {
        try {
            Voucher updatedVoucher = voucherService.deleteVoucher(id);
            VoucherResponse response = convertToVoucherResponse(updatedVoucher);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Response.builder()
                            .status(HttpStatus.NOT_FOUND.toString())
                            .mess(e.getMessage())
                            .build());
        }
    }

    @PutMapping("/end-early/{id}")
    public ResponseEntity<?> endVoucherEarly(@PathVariable("id") Long id,
                                             @RequestParam(value = "userId", required = false) Long userId) {
        try {
            Voucher updatedVoucher = voucherService.endVoucherEarly(id, Optional.ofNullable(userId).orElse(1L));
            VoucherResponse response = convertToVoucherResponse(updatedVoucher);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Response.builder()
                            .status(HttpStatus.NOT_FOUND.toString())
                            .mess(e.getMessage())
                            .build());
        }
    }

    @PutMapping("/reactivate/{id}")
    public ResponseEntity<?> reactivateVoucher(@PathVariable("id") Long id,
                                               @RequestParam(value = "userId", required = false) Long userId) {
        try {
            Voucher updatedVoucher = voucherService.reactivateVoucher(id, Optional.ofNullable(userId).orElse(1L));
            VoucherResponse response = convertToVoucherResponse(updatedVoucher);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Response.builder()
                            .status(HttpStatus.NOT_FOUND.toString())
                            .mess(e.getMessage())
                            .build());
        }
    }

    @GetMapping("/check-expired")
    public ResponseEntity<?> checkExpiredVouchers() {
        try {
            voucherService.checkAndExpireVouchers();
            return ResponseEntity.ok(Response.builder()
                    .status(HttpStatus.OK.toString())
                    .mess("Checked and updated expired vouchers successfully.")
                    .build());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Response.builder()
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.toString())
                            .mess(e.getMessage())
                            .build());
        }
    }

    private VoucherResponse convertToVoucherResponse(Voucher voucher) {
        VoucherResponse response = new VoucherResponse();
        BeanUtils.copyProperties(voucher, response);
        return response;
    }
}
