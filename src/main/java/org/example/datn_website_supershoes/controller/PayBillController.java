package org.example.datn_website_supershoes.controller;

import jakarta.persistence.criteria.Predicate;
import org.example.datn_website_supershoes.dto.response.PayBillResponse;
import org.example.datn_website_supershoes.model.PayBill;
import org.example.datn_website_supershoes.service.PayBillService;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/pay-bill")
public class PayBillController {

    @Autowired
    private PayBillService payBillService;

    @GetMapping("/list-pay-bills")
    public Page<PayBillResponse> getAllPayBills(
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "tradingCode", required = false) String tradingCode,
            @RequestParam(value = "type", required = false) Integer type,
            @RequestParam(value = "createdAt", required = false) String createdAtStr,
            @RequestParam(value = "codeBill", required = false) String codeBill,  // Add codeBill param
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "createdAt") String sortField,
            @RequestParam(value = "sortDirection", defaultValue = "DESC") String sortDirection
    ) {
        Date createdAt = parseDate(createdAtStr);

        Specification<PayBill> spec = (root, query, criteriaBuilder) -> {
            Predicate p = criteriaBuilder.conjunction();

            if (status != null && !status.isEmpty()) {
                p = criteriaBuilder.and(p, criteriaBuilder.equal(root.get("status"), status));
            }

            if (tradingCode != null && !tradingCode.isEmpty()) {
                p = criteriaBuilder.and(p, criteriaBuilder.equal(root.get("tradingCode"), tradingCode));
            }

            if (type != null) {
                p = criteriaBuilder.and(p, criteriaBuilder.equal(root.get("type"), type));
            }

            if (createdAt != null) {
                String formattedCreatedAt = new SimpleDateFormat("yyyy-MM-dd").format(createdAt);
                p = criteriaBuilder.and(p, criteriaBuilder.like(root.get("createdAt").as(String.class), "%" + formattedCreatedAt + "%"));
            }

            if (codeBill != null && !codeBill.isEmpty()) {
                p = criteriaBuilder.and(p, criteriaBuilder.equal(root.get("bill").get("codeBill"), codeBill));
            }

            return p;
        };

        // Setting the sorting direction
        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));

        return payBillService.getPayBills(spec, pageable);
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

    @GetMapping("/detail/{id}")
    public ResponseEntity<PayBill> getPayBillById(@PathVariable Long id) {
        Optional<PayBill> payBill = payBillService.getPayBillById(id);
        return payBill.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/add")
    public ResponseEntity<Map<String, Object>> createPayBill(@RequestBody PayBill payBill) {
        PayBill createdPayBill = payBillService.createPayBill(payBill);
        Map<String, Object> response = new HashMap<>();
        response.put("DT", createdPayBill);
        response.put("EC", 0);
        response.put("EM", "PayBill added successfully");

        return ResponseEntity.ok(response);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Map<String, Object>> updatePayBill(@PathVariable Long id, @RequestBody PayBill payBillDetails) {
        PayBill updatedPayBill = payBillService.updatePayBill(id, payBillDetails);
        Map<String, Object> response = new HashMap<>();
        response.put("DT", updatedPayBill);
        response.put("EC", 0);
        response.put("EM", "PayBill updated successfully");

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deletePayBill(@PathVariable Long id) {
        try {
            payBillService.deletePayBill(id);
            return ResponseEntity.status(HttpStatus.OK).body("PayBill deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting PayBill");
        }
    }
}
