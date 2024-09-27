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
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<Page<VoucherResponse>> getAllVouchers(
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "searchTerm", required = false) String searchTerm,
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate endDate,
            @RequestParam(value = "filterByStartAt", defaultValue = "false") boolean filterByStartAt,
            @RequestParam(value = "filterByEndAt", defaultValue = "false") boolean filterByEndAt,
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

            if (filterByStartAt) {
                if (startDate != null && endDate != null) {
                    Predicate startAtPredicate = criteriaBuilder.between(root.get("startAt"), startDate.atStartOfDay(), endDate.atTime(23, 59, 59));
                    p = criteriaBuilder.and(p, startAtPredicate);
                } else if (startDate != null) {
                    Predicate startAtPredicate = criteriaBuilder.greaterThanOrEqualTo(root.get("startAt"), startDate.atStartOfDay());
                    p = criteriaBuilder.and(p, startAtPredicate);
                } else if (endDate != null) {
                    Predicate startAtPredicate = criteriaBuilder.lessThanOrEqualTo(root.get("startAt"), endDate.atTime(23, 59, 59));
                    p = criteriaBuilder.and(p, startAtPredicate);
                }
            }

            if (filterByEndAt) {
                if (startDate != null && endDate != null) {
                    Predicate endAtPredicate = criteriaBuilder.between(root.get("endAt"), startDate.atStartOfDay(), endDate.atTime(23, 59, 59));
                    p = criteriaBuilder.and(p, endAtPredicate);
                } else if (startDate != null) {
                    Predicate endAtPredicate = criteriaBuilder.greaterThanOrEqualTo(root.get("endAt"), startDate.atStartOfDay());
                    p = criteriaBuilder.and(p, endAtPredicate);
                } else if (endDate != null) {
                    Predicate endAtPredicate = criteriaBuilder.lessThanOrEqualTo(root.get("endAt"), endDate.atTime(23, 59, 59));
                    p = criteriaBuilder.and(p, endAtPredicate);
                }
            }

            if (filterByStartAt && filterByEndAt && startDate != null && endDate != null) {
                Predicate startAtPredicate = criteriaBuilder.between(root.get("startAt"), startDate.atStartOfDay(), endDate.atTime(23, 59, 59));
                Predicate endAtPredicate = criteriaBuilder.between(root.get("endAt"), startDate.atStartOfDay(), endDate.atTime(23, 59, 59));
                p = criteriaBuilder.and(p, criteriaBuilder.or(startAtPredicate, endAtPredicate));
            }

            return p;
        };

        Pageable pageable = PageRequest.of(page, size);
        Page<VoucherResponse> vouchers = voucherService.getVouchers(spec, pageable);
        return ResponseEntity.ok(vouchers);
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
    public ResponseEntity<?> updateVoucher(@PathVariable("id") long id, @RequestBody VoucherRequest voucherRequest) {
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
