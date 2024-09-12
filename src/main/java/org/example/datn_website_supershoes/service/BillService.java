package org.example.datn_website_supershoes.service;

import org.example.datn_website_supershoes.Enum.Status;
import org.example.datn_website_supershoes.dto.response.BillResponse;
import org.example.datn_website_supershoes.model.Bill;
import org.example.datn_website_supershoes.repository.BillRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BillService {

    @Autowired
    private BillRepository billRepository;

    public Bill createBill(Bill bill) {
        return billRepository.save(bill);
    }

    public List<BillResponse> getAllBills() {
        return billRepository.listBillResponseByStatus();
    }

    //    public List<BillResponse> getAllBills() {
//        return billRepository.listBillResponseByStatus(Status.ACTIVE.toString());
//    }
    public Page<BillResponse> getBills(Specification<Bill> spec, Pageable pageable) {
        return billRepository.findAll(spec, pageable).map(this::convertToBillResponse);
    }

    public List<Bill> getAllBills2() {
        return billRepository.findAll();
    }

    public Optional<Bill> getBillById(Long id) {
        return billRepository.findById(id);
    }

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
        if (bill.getGuest() != null) {
            existingBill.setGuest(bill.getGuest());
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

    private BillResponse convertToBillResponse(Bill bill) {
        BillResponse response = new BillResponse();

        // Copy simple attributes
        response.setId(bill.getId());
        response.setNameCustomer(bill.getNameCustomer());
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

        // Copy attributes from related objects if not null
        if (bill.getGuest() != null) {
            response.setIdGuest(bill.getGuest().getId());
        }
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


}
