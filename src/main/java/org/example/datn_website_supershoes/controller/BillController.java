package org.example.datn_website_supershoes.controller;

import org.example.datn_website_supershoes.dto.response.BillResponse;
import org.example.datn_website_supershoes.dto.response.BillSummaryResponse;
import org.example.datn_website_supershoes.model.Bill;
import org.example.datn_website_supershoes.service.BillService;
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

import jakarta.persistence.criteria.Predicate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/bill")
public class BillController {

    @Autowired
    private BillService billService;

    @GetMapping("/detail/{codeBill}")
    public ResponseEntity<BillResponse> getBillByCodeBill(@PathVariable String codeBill) {
        Optional<BillResponse> billResponse = billService.getBillByCodeBill(codeBill);
        return billResponse.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // New method for fetching Bill summary by codeBill
    @GetMapping("/list-bill-summaries")
    public Page<BillSummaryResponse> getBillSummaries(
            @RequestParam(value = "codeBill", required = false) String codeBill,
            @RequestParam(value = "nameCustomer", required = false) String nameCustomer,
            @RequestParam(value = "phoneNumber", required = false) String phoneNumber,
            @RequestParam(value = "address", required = false) String address,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "createdAt") String sortField,
            @RequestParam(value = "sortDirection", defaultValue = "DESC") String sortDirection
    ) {
        Specification<Bill> spec = (root, query, criteriaBuilder) -> {
            Predicate p = criteriaBuilder.conjunction();

            if (codeBill != null && !codeBill.isEmpty()) {
                p = criteriaBuilder.and(p, criteriaBuilder.equal(root.get("codeBill"), codeBill));
            }

            if (nameCustomer != null && !nameCustomer.isEmpty()) {
                p = criteriaBuilder.and(p, criteriaBuilder.like(root.get("nameCustomer"), "%" + nameCustomer + "%"));
            }

            if (phoneNumber != null && !phoneNumber.isEmpty()) {
                p = criteriaBuilder.and(p, criteriaBuilder.like(root.get("phoneNumber"), "%" + phoneNumber + "%"));
            }

            if (address != null && !address.isEmpty()) {
                p = criteriaBuilder.and(p, criteriaBuilder.like(root.get("address"), "%" + address + "%"));
            }

            return p;
        };

        // Setting the sorting direction
        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));

        return billService.getBillSummaries(spec, pageable);
    }


    @GetMapping("/list-bills")
    public Page<BillResponse> getAllBills(
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "codeBill", required = false) String codeBill,
            @RequestParam(value = "type", required = false) Integer type,
            @RequestParam(value = "deliveryDate", required = false) String deliveryDateStr,
            @RequestParam(value = "receiveDate", required = false) String receiveDateStr,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "createdAt") String sortField,
            @RequestParam(value = "sortDirection", defaultValue = "DESC") String sortDirection
    ) {
        Date deliveryDate = parseDate(deliveryDateStr);
        Date receiveDate = parseDate(receiveDateStr);

        Specification<Bill> spec = (root, query, criteriaBuilder) -> {
            Predicate p = criteriaBuilder.conjunction();

            // Filtering by status if provided
            if (status != null && !status.isEmpty()) {
                p = criteriaBuilder.and(p, criteriaBuilder.equal(root.get("status"), status));
            }

            // Partial matching for codeBill (search by each letter)
            if (codeBill != null && !codeBill.isEmpty()) {
                p = criteriaBuilder.and(p, criteriaBuilder.like(root.get("codeBill"), "%" + codeBill + "%"));
            }

            // Filtering by type if provided
            if (type != null) {
                p = criteriaBuilder.and(p, criteriaBuilder.equal(root.get("type"), type));
            }

            // Filtering by deliveryDate using >= comparison
            if (deliveryDate != null) {
                p = criteriaBuilder.and(p, criteriaBuilder.greaterThanOrEqualTo(root.get("deliveryDate").as(Date.class), deliveryDate));
            }

            // Filtering by receiveDate using <= comparison
            if (receiveDate != null) {
                p = criteriaBuilder.and(p, criteriaBuilder.lessThanOrEqualTo(root.get("receiveDate").as(Date.class), receiveDate));
            }

            return p;
        };

        // Setting the sorting direction
        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));

        return billService.getBills(spec, pageable);
    }

    // Helper method to parse dates
    private Date parseDate(String dateStr) {
        if (dateStr == null) return null;

        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        try {
            if (dateStr.length() == 10) {
                return dateFormat.parse(dateStr);
            } else {
                return dateTimeFormat.parse(dateStr);
            }
        } catch (ParseException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid date format: " + dateStr + ". Expected formats: yyyy-MM-dd or yyyy-MM-dd'T'HH:mm:ss", e);
        }
    }


    @PostMapping("/add")
    public ResponseEntity<Map<String, Object>> createBill(@RequestBody Bill bill) {
        Bill createdBill = billService.createBill(bill);
        Map<String, Object> response = new HashMap<>();
        response.put("DT", createdBill);
        response.put("EC", 0);
        response.put("EM", "Bill added successfully");

        return ResponseEntity.ok(response);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Map<String, Object>> updateBill(@PathVariable Long id, @RequestBody Bill billDetails) {
        Bill updatedBill = billService.updateBill(id, billDetails);
        Map<String, Object> response = new HashMap<>();
        response.put("DT", updatedBill);
        response.put("EC", 0);
        response.put("EM", "Bill updated successfully");

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteBill(@PathVariable Long id) {
        try {
            billService.deleteBill(id);
            return ResponseEntity.status(HttpStatus.OK).body("Bill deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting bill");
        }
    }
}
