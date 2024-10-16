package org.example.datn_website_supershoes.controller;

import jakarta.persistence.criteria.Predicate;
import jakarta.validation.Valid;
import org.example.datn_website_supershoes.dto.request.BillDetailRequest;
import org.example.datn_website_supershoes.dto.response.BillDetailResponse;
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
        // Create the specification for filtering
        Specification<BillDetail> spec = (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();

            if (status != null && !status.isEmpty()) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("status"), status));
            }

            if (codeBill != null && !codeBill.isEmpty()) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("bill").get("codeBill"), codeBill));
            }

            if (quantity != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("quantity"), quantity));
            }

            return predicate;
        };

        // Validate and set the sorting direction
        Sort.Direction direction;
        try {
            direction = Sort.Direction.fromString(sortDirection);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid sort direction");
        }

        // Validate the sort field
        List<String> validSortFields = Arrays.asList("createdAt", "status", "quantity", "codeBill"); // Add valid fields here
        if (!validSortFields.contains(sortField)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid sort field");
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));

        // Call the service method to get the filtered results
        return billDetailService.getBillDetails(spec, pageable);
    }


    @GetMapping("/detail/{id}")
    public ResponseEntity<BillDetail> getBillDetailById(@PathVariable Long id) {
        Optional<BillDetail> billDetail = billDetailService.getBillDetailById(id);
        return billDetail.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/add")
    public ResponseEntity<Map<String, Object>> createOrUpdateBillDetail(@Valid @RequestBody BillDetailRequest billDetailRequest) {
        Map<String, Object> response = new HashMap<>();
        try {
            // Attempt to create or update the BillDetail
            BillDetail billDetail = billDetailService.createOrUpdateBillDetail(billDetailRequest);
            response.put("DT", billDetail);
            response.put("EC", 0); // No error
            response.put("EM", "BillDetail processed successfully");
        } catch (RuntimeException ex) {
            // If an error occurs, return the error message
            response.put("DT", null);
            response.put("EC", 1); // Error code
            response.put("EM", ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

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
    @DeleteMapping("/delete-by-product-and-color")
    public ResponseEntity<Map<String, Object>> deleteBillDetailByProductAndColor(
            @RequestParam String productCode,
            @RequestParam String nameColor,
            @RequestParam String nameSize) { // Added nameSize parameter
        Map<String, Object> response = new HashMap<>();
        try {
            // Call the service method and pass the nameSize as well
            billDetailService.deleteBillDetailAndUpdateProduct(productCode, nameColor, nameSize);
            response.put("EC", 0); // No error
            response.put("EM", "BillDetail deleted and product quantity updated successfully.");
        } catch (RuntimeException ex) {
            response.put("EC", 1); // Error code
            response.put("EM", ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        return ResponseEntity.ok(response);
    }


}
