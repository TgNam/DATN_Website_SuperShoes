package org.example.datn_website_supershoes.controller;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.example.datn_website_supershoes.dto.request.BillRequest;
import org.example.datn_website_supershoes.dto.response.BillResponse;
import org.example.datn_website_supershoes.dto.response.BillStatisticalPieResponse;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import jakarta.persistence.criteria.Predicate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/bill")
public class BillController {

    @Autowired
    BillService billService;

    @GetMapping("/statistics/completed")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE','CUSTOMER')")
    public ResponseEntity<?> getCompletedBillStatistics() {
        try{
        List<BillStatisticalPieResponse> statistics = billService.getCompletedBillStatistics();
        return ResponseEntity.ok(statistics);
        }catch (RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/update-status-note/{codeBill}")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    public ResponseEntity<?> updateBillStatusAndNote(
            @PathVariable String codeBill,
            @RequestParam String status,
            @RequestParam String note) {
        try {
            // Call the service to update status and note
            billService.updateBillStatusAndNoteByCode(codeBill, status, note);
            return ResponseEntity.ok("Bill status and note updated successfully.");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Bill with code " + codeBill + " not found.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/update-status/{codeBill}")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    public ResponseEntity<?> updateBillStatus(@PathVariable String codeBill) {
        try {
            billService.updateBillStatus(codeBill);
            return ResponseEntity.ok("Bill status updated successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/detail/{codeBill}")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE','CUSTOMER')")
    public ResponseEntity<?> getBillByCodeBill(@PathVariable String codeBill) {
        try {
            Optional<BillResponse> billResponse = billService.getBillByCodeBill(codeBill);
            return ResponseEntity.ok(billResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


    // New method for fetching Bill summary by codeBill
    // tu sua
    @GetMapping("/list-bill-summaries")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE','CUSTOMER')")
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
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE','CUSTOMER')")
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
            } else {
                p = criteriaBuilder.and(p, criteriaBuilder.notEqual(root.get("status"), "WAITING_FOR_PAYMENT"));
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
    public Date parseDate(String dateStr) {
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

    @PutMapping("/updateCodeBill/{codeBill}")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    public ResponseEntity<?> updateBill2(
            @PathVariable String codeBill,
            @RequestBody @Valid BillRequest billRequest,
            BindingResult bindingResult) {

        // Check for validation errors in the provided fields
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getFieldErrors()
                    .stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
                    .collect(Collectors.toList());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }

        // Check if path variable and request body codeBill match
        if (billRequest.getCodeBill() != null && !codeBill.equals(billRequest.getCodeBill())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("The code in the URL does not match the code in the request body.");
        }

        try {
            // Fetch the existing bill
            Bill bill = billService.findBillByCode(codeBill);

            if (bill == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Bill with code " + codeBill + " not found.");
            }

            // Update fields using helper method
            updateBillFields(bill, billRequest);

            // Save the updated bill
            Bill updatedBill = billService.save(bill);

            // Convert to response DTO
            BillSummaryResponse updatedBillResponse = new BillSummaryResponse(updatedBill);
            return ResponseEntity.ok(updatedBillResponse);

        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Bill with code " + codeBill + " not found.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating bill with code " + codeBill);
        }
    }

    // Helper method to update only non-null fields
    public void updateBillFields(Bill bill, BillRequest billRequest) {
        if (billRequest.getNameCustomer() != null) {
            bill.setNameCustomer(billRequest.getNameCustomer());
        }
        if (billRequest.getPhoneNumber() != null) {
            bill.setPhoneNumber(billRequest.getPhoneNumber());
        }
        if (billRequest.getAddress() != null) {
            bill.setAddress(billRequest.getAddress());
        }
        if (billRequest.getNote() != null) {
            bill.setNote(billRequest.getNote());
        }
        if (billRequest.getType() != null) {
            bill.setType(billRequest.getType());
        }
        if (billRequest.getDeliveryDate() != null) {
            bill.setDeliveryDate(billRequest.getDeliveryDate());
        }
        if (billRequest.getReceiveDate() != null) {
            bill.setReceiveDate(billRequest.getReceiveDate());
        }
        if (billRequest.getTotalMerchandise() != null) {
            bill.setTotalMerchandise(billRequest.getTotalMerchandise());
        }
        if (billRequest.getPriceDiscount() != null) {
            bill.setPriceDiscount(billRequest.getPriceDiscount());
        }
        if (billRequest.getTotalAmount() != null) {
            bill.setTotalAmount(billRequest.getTotalAmount());
        }
        if (billRequest.getStatus() != null) {
            bill.setStatus(billRequest.getStatus());
        }
    }


    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    public ResponseEntity<String> deleteBill(@PathVariable Long id) {
        try {
            billService.deleteBill(id);
            return ResponseEntity.status(HttpStatus.OK).body("Bill deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting bill");
        }
    }
}
