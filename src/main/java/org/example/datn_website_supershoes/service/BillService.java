package org.example.datn_website_supershoes.service;

import jakarta.persistence.EntityNotFoundException;
import org.example.datn_website_supershoes.dto.request.BillRequest;
import org.example.datn_website_supershoes.dto.response.BillResponse;
import org.example.datn_website_supershoes.dto.response.BillSummaryResponse;
import org.example.datn_website_supershoes.model.Bill;
import org.example.datn_website_supershoes.repository.BillRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BillService {

    @Autowired
    private BillRepository billRepository;

    // Method to create a new Bill
    public Bill createBill(Bill bill) {
        return billRepository.save(bill);
    }

    // Fetch all BillResponse objects with ACTIVE status
//    public List<BillResponse> getAllBills() {
//        return billRepository.listBillResponseByStatus();
//    }

    // Paginated fetching of bills using Specification for filtering
    public Page<BillResponse> getBills(Specification<Bill> spec, Pageable pageable) {
        return billRepository.findAll(spec, pageable).map(this::convertToBillResponse);
    }

    // Fetch all Bill entities
    public List<Bill> getAllBills2() {
        return billRepository.findAll();
    }

    // Fetch Bill by ID
    public Optional<Bill> getBillById(Long id) {
        return billRepository.findById(id);
    }

    public Bill updateCodeBill(String codeBill, Bill bill) {
        // Find the existing bill by codeBill
        Bill existingBill = billRepository.findByCodeBill(codeBill)
                .orElseThrow(() -> new RuntimeException("Bill not found with codeBill: " + codeBill));

        // List of properties to ignore during update
        String[] ignoredProperties = {"id", "createdAt", "createdBy"};
        BeanUtils.copyProperties(bill, existingBill, ignoredProperties);

        // Set related entities if they are not null
        if (bill.getVoucher() != null) {
            existingBill.setVoucher(bill.getVoucher());
        }
        if (bill.getCustomer() != null) {
            existingBill.setCustomer(bill.getCustomer());
        }
        if (bill.getEmployees() != null) {
            existingBill.setEmployees(bill.getEmployees());
        }

        if (bill.getPayBills() != null) {
            existingBill.setPayBills(bill.getPayBills());
        }
        if (bill.getBillHistories() != null) {
            existingBill.setBillHistories(bill.getBillHistories());
        }
        if (bill.getBillDetails() != null) {
            existingBill.setBillDetails(bill.getBillDetails());
        }

        // Save the updated bill entity
        return billRepository.save(existingBill);
    }

    // Update an existing Bill
    public Bill updateBill(Long id, Bill bill) {
        Bill existingBill = billRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bill not found"));

        // List of properties to ignore during update
        String[] ignoredProperties = {"id", "createdAt", "createdBy"};
        BeanUtils.copyProperties(bill, existingBill, ignoredProperties);

        // Set related entities if they are not null
        if (bill.getVoucher() != null) {
            existingBill.setVoucher(bill.getVoucher());
        }
        if (bill.getCustomer() != null) {
            existingBill.setCustomer(bill.getCustomer());
        }
        if (bill.getEmployees() != null) {
            existingBill.setEmployees(bill.getEmployees());
        }

        if (bill.getPayBills() != null) {
            existingBill.setPayBills(bill.getPayBills());
        }
        if (bill.getBillHistories() != null) {
            existingBill.setBillHistories(bill.getBillHistories());
        }
        if (bill.getBillDetails() != null) {
            existingBill.setBillDetails(bill.getBillDetails());
        }

        return billRepository.save(existingBill);
    }


    // Delete a Bill by ID
    public void deleteBill(Long id) {
        billRepository.deleteById(id);
    }

    // Fetch BillResponse by codeBill
    public Optional<BillResponse> getBillByCodeBill(String codeBill) {
        Optional<Bill> optionalBill = billRepository.findByCodeBill(codeBill);
        return optionalBill.map(this::convertToBillResponse);
    }

    // New method to fetch BillSummaryResponse by codeBill
    public Optional<BillSummaryResponse> getBillSummaryByCodeBill(String codeBill) {
        return billRepository.findBillSummaryByCodeBill(codeBill);
    }

    // Helper method to convert Bill entity to BillResponse DTO
    private BillResponse convertToBillResponse(Bill bill) {
        BillResponse response = new BillResponse();

        // Copy simple attributes
        response.setId(bill.getId());
        response.setCodeBill(bill.getCodeBill());
        response.setNameCustomer(bill.getCustomer() != null ? bill.getCustomer().getName() : null);
        response.setPhoneNumber(bill.getPhoneNumber());
        response.setAddress(bill.getAddress());
        response.setNote(bill.getNote());
        response.setType(bill.getType());
        response.setDeliveryDate(bill.getDeliveryDate());
        response.setReceiveDate(bill.getReceiveDate());
        response.setTotalMerchandise(bill.getTotalMerchandise());
        response.setPriceDiscount(bill.getPriceDiscount());
        response.setTotalAmount(bill.getTotalAmount());
        response.setStatus(bill.getStatus());
        response.setCreatedAt(bill.getCreatedAt());

        // Copy related object data if present

        if (bill.getVoucher() != null) {
            response.setIdVoucher(bill.getVoucher().getId());
            response.setNameVoucher(bill.getVoucher().getName());
        }
        if (bill.getCustomer() != null) {
            response.setIdCustomer(bill.getCustomer().getId());
        }
        if (bill.getEmployees() != null) {
            response.setIdEmployees(bill.getEmployees().getId());
            response.setNameEmployees(bill.getEmployees().getName());
        }

        return response;
    }

    public Page<BillSummaryResponse> getBillSummaries(Specification<Bill> spec, Pageable pageable) {
        return billRepository.findAll(spec, pageable).map(this::convertToBillSummaryResponse);
    }

    // Helper method to convert Bill entity to BillSummaryResponse DTO
    private BillSummaryResponse convertToBillSummaryResponse(Bill bill) {
        return new BillSummaryResponse(
                bill.getCodeBill(),
                bill.getStatus(),
                bill.getNameCustomer(),
                bill.getAddress(),
                bill.getPhoneNumber(),
                bill.getNote()
        );
    }
    public BillSummaryResponse updateBillByCode(String codeBill, BillRequest billRequest) {
        // Fetch the Bill entity by codeBill
        Bill bill = billRepository.findByCodeBill(codeBill)
                .orElseThrow(() -> new EntityNotFoundException("Bill not found with code: " + codeBill));

        // Update the bill fields with new values from billRequest
        bill.setNameCustomer(billRequest.getNameCustomer());
        bill.setPhoneNumber(billRequest.getPhoneNumber());
        bill.setAddress(billRequest.getAddress());
        bill.setNote(billRequest.getNote());

        // Save the updated bill entity
        bill = billRepository.save(bill);

        // Convert Bill entity to BillSummaryResponse DTO
        return new BillSummaryResponse(bill);
    }
    public Bill findBillByCode(String codeBill) {
        return billRepository.findByCodeBill(codeBill)
                .orElseThrow(() -> new EntityNotFoundException("Bill not found with code: " + codeBill));
    }

    public Bill save(Bill bill) {
        return billRepository.save(bill);
    }

}
