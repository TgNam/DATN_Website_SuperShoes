package org.example.datn_website_supershoes.controller;

import jakarta.persistence.criteria.Predicate;
import jakarta.validation.Valid;
import org.example.datn_website_supershoes.dto.request.BillDetailRequest;
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
    public ResponseEntity<Map<String, Object>> createBillDetail(@Valid @RequestBody BillDetailRequest billDetailRequest) {
        // Convert BillDetailRequest to BillDetail entity
        BillDetail createdBillDetail = billDetailService.createBillDetail(billDetailRequest);

        // Create response map
        Map<String, Object> response = new HashMap<>();
        response.put("DT", createdBillDetail);
        response.put("EC", 0);
        response.put("EM", "BillDetail added successfully");

        return ResponseEntity.ok(response);
    }


    @PutMapping("/update")
    public ResponseEntity<Map<String, Object>> updateBillDetail(@Valid @RequestBody BillDetailRequest billDetailRequest) {
        Map<String, Object> response = new HashMap<>();
        try {
            // Update BillDetail
            BillDetail updatedBillDetail = billDetailService.updateBillDetail(billDetailRequest);

            // Success response
            response.put("DT", updatedBillDetail);
            response.put("EC", 0); // EC = 0: No error
            response.put("EM", "BillDetail updated successfully");

            return ResponseEntity.ok(response);

        } catch (RuntimeException ex) {
            // Handle runtime exceptions like insufficient stock, BillDetail not found, etc.
            response.put("DT", null);
            response.put("EC", 1); // EC = 1: Error code for runtime errors
            response.put("EM", ex.getMessage()); // Provide specific error message

            // Log the exception (you can use any logger, such as log4j or slf4j)
            System.err.println("Error occurred: " + ex.getMessage());
            ex.printStackTrace(); // Log stack trace for debugging purposes

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception ex) {
            // Handle unexpected errors
            response.put("DT", null);
            response.put("EC", 2); // EC = 2: Error code for unexpected errors
            response.put("EM", "An unexpected error occurred");

            // Log the unexpected error for further investigation
            System.err.println("Unexpected error: " + ex.getMessage());
            ex.printStackTrace();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
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
