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

    public void updateBillStatusAndNoteByCode(String codeBill, String status, String note) {
        Bill bill = billRepository.findByCodeBill(codeBill)
                .orElseThrow(() -> new RuntimeException("Bill not found with code: " + codeBill));

        // Update the status and note fields
        bill.setStatus(status);
        bill.setNote(note);

        // Save the updated bill
        billRepository.save(bill);
    }

    public void updateBillStatus(String codeBill) {
        Bill bill = billRepository.findByCodeBill(codeBill)
                .orElseThrow(() -> new RuntimeException("Bill not found"));

        // Assuming you have some logic for updating status
        if (bill.getStatus().equals("PENDING")) {
            bill.setStatus("CONFIRMED");
        } else if (bill.getStatus().equals("CONFIRMED")) {
            bill.setStatus("SHIPPED");
        } else if (bill.getStatus().equals("SHIPPED")) {
            bill.setStatus("COMPLETED");
        } else {
            throw new RuntimeException("Invalid bill status");
        }

        billRepository.save(bill);
    }


    public Bill createBill(Bill bill) {
        return billRepository.save(bill);
    }


    public Page<BillResponse> getBills(Specification<Bill> spec, Pageable pageable) {
        return billRepository.findAll(spec, pageable).map(this::convertToBillResponse);
    }


    public List<Bill> getAllBills2() {
        return billRepository.findAll();
    }


    public Optional<Bill> getBillById(Long id) {
        return billRepository.findById(id);
    }

    public Bill updateCodeBill(String codeBill, Bill bill) {

        Bill existingBill = billRepository.findByCodeBill(codeBill)
                .orElseThrow(() -> new RuntimeException("Bill not found with codeBill: " + codeBill));


        String[] ignoredProperties = {"id", "createdAt", "createdBy"};
        BeanUtils.copyProperties(bill, existingBill, ignoredProperties);


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


        String[] ignoredProperties = {"id", "createdAt", "createdBy"};
        BeanUtils.copyProperties(bill, existingBill, ignoredProperties);


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



    public void deleteBill(Long id) {
        billRepository.deleteById(id);
    }


    public Optional<BillResponse> getBillByCodeBill(String codeBill) {
        Optional<Bill> optionalBill = billRepository.findByCodeBill(codeBill);
        return optionalBill.map(this::convertToBillResponse);
    }


    public Optional<BillSummaryResponse> getBillSummaryByCodeBill(String codeBill) {
        return billRepository.findBillSummaryByCodeBill(codeBill);
    }

    private BillResponse convertToBillResponse(Bill bill) {
        BillResponse response = new BillResponse();


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

        Bill bill = billRepository.findByCodeBill(codeBill)
                .orElseThrow(() -> new EntityNotFoundException("Bill not found with code: " + codeBill));


        bill.setNameCustomer(billRequest.getNameCustomer());
        bill.setPhoneNumber(billRequest.getPhoneNumber());
        bill.setAddress(billRequest.getAddress());
        bill.setNote(billRequest.getNote());


        bill = billRepository.save(bill);


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
