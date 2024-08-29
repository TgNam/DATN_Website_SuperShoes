package org.example.datn_website_supershoes.controller;

import org.example.datn_website_supershoes.dto.response.BillResponse;
import org.example.datn_website_supershoes.model.Bill;
import org.example.datn_website_supershoes.service.BillService;
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
import jakarta.persistence.criteria.Predicate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/bill")
public class BillController {

    @Autowired
    private BillService billService;

    //    @GetMapping("/list-billC")
//    public ResponseEntity<Map<String, Object>> getAllBills() {
//        List<BillResponse> listBills = billService.getAllBills();
//        Map<String, Object> response = new HashMap<>();
//        response.put("DT", listBills);
//        response.put("EC", 0);
//        response.put("EM", "Get all bills succeed");
//
//        return ResponseEntity.ok(response);
//    }

    @GetMapping("/list-bills")
    public Page<BillResponse> getAllBills(
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "id", required = false) Integer id,
            @RequestParam(value = "type", required = false) Integer type,
            @RequestParam(value = "deliveryDate", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME, pattern = "yyyy-MM-dd'T'HH:mm:ss") Date deliveryDate,
            @RequestParam(value = "receiveDate", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME, pattern = "yyyy-MM-dd'T'HH:mm:ss") Date receiveDate,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        Specification<Bill> spec = (root, query, criteriaBuilder) -> {
            Predicate p = criteriaBuilder.conjunction();

            if (status != null && !status.isEmpty()) {
                p = criteriaBuilder.and(p, criteriaBuilder.equal(root.get("status"), status));
            }

            if (id != null && id >= 0) {
                p = criteriaBuilder.and(p, criteriaBuilder.equal(root.get("id"), id));
            }

            if (type != null) {
                p = criteriaBuilder.and(p, criteriaBuilder.equal(root.get("type"), type));
            }

            if (deliveryDate != null) {
                p = criteriaBuilder.and(p, criteriaBuilder.equal(root.get("deliveryDate"), new java.sql.Timestamp(deliveryDate.getTime())));
            }

            if (receiveDate != null) {
                p = criteriaBuilder.and(p, criteriaBuilder.equal(root.get("receiveDate"), new java.sql.Timestamp(receiveDate.getTime())));
            }

            return p;
        };

        Pageable pageable = PageRequest.of(page, size);
        return billService.getBills(spec, pageable);
    }



    @GetMapping("/list-bill1")
    public List<BillResponse> getAllBill2() {


        return billService.getAllBills();
    }

    @GetMapping("/list-bill")
    public List<Bill> getAll() {

        return billService.getAllBills2();
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<Bill> getBillById(@PathVariable Long id) {
        Optional<Bill> bill = billService.getBillById(id);
        return bill.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
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
