package org.example.datn_website_supershoes.controller;

import jakarta.persistence.criteria.Predicate;
import org.example.datn_website_supershoes.dto.response.BillDetailResponse;
import org.example.datn_website_supershoes.dto.response.Response;
import org.example.datn_website_supershoes.model.BillDetail;
import org.example.datn_website_supershoes.service.BillDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@RestController
@RequestMapping("/bill-detail")
public class BillDetailController {

    @Autowired
    private BillDetailService billDetailService;

    @GetMapping("/list-bill-details")
    public Page<BillDetailResponse> getAllBillDetails(
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "codeBill", required = false) String codeBill,
            @RequestParam(value = "quantity", required = false) Integer quantity,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "createdAt") String sortField,
            @RequestParam(value = "sortDirection", defaultValue = "DESC") String sortDirection
    ) {
        Specification<BillDetail> spec = (root, query, criteriaBuilder) -> {
            Predicate p = criteriaBuilder.conjunction();

            if (status != null && !status.isEmpty()) {
                p = criteriaBuilder.and(p, criteriaBuilder.equal(root.get("status"), status));
            }

            if (codeBill != null && !codeBill.isEmpty()) {
                p = criteriaBuilder.and(p, criteriaBuilder.equal(root.get("bill").get("codeBill"), codeBill));
            }

            if (quantity != null) {
                p = criteriaBuilder.and(p, criteriaBuilder.equal(root.get("quantity"), quantity));
            }

            return p;
        };

        // Set the sorting direction
        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));

        return billDetailService.getBillDetails(spec, pageable);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<BillDetail> getBillDetailById(@PathVariable Long id) {
        Optional<BillDetail> billDetail = billDetailService.getBillDetailById(id);
        return billDetail.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/add")
    public ResponseEntity<Map<String, Object>> createBillDetail(@RequestBody BillDetail billDetail) {
        BillDetail createdBillDetail = billDetailService.createBillDetail(billDetail);
        Map<String, Object> response = new HashMap<>();
        response.put("DT", createdBillDetail);
        response.put("EC", 0);
        response.put("EM", "BillDetail added successfully");

        return ResponseEntity.ok(response);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Map<String, Object>> updateBillDetail(@PathVariable Long id, @RequestBody BillDetail billDetailDetails) {
        BillDetail updatedBillDetail = billDetailService.updateBillDetail(id, billDetailDetails);
        Map<String, Object> response = new HashMap<>();
        response.put("DT", updatedBillDetail);
        response.put("EC", 0);
        response.put("EM", "BillDetail updated successfully");

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteBillDetail(@PathVariable Long id) {
        try {
            billDetailService.deleteBillDetail(id);
            return ResponseEntity.status(HttpStatus.OK).body("BillDetail deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting BillDetail");
        }
    }
    @DeleteMapping("/delete-by-product-code")
    public ResponseEntity<?> deleteBillDetailsByProductCode(@RequestParam("productCode") String productCode) {
        try {
            billDetailService.deleteBillDetailsByProductCode(productCode);
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
