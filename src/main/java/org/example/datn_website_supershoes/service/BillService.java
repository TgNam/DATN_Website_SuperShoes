package org.example.datn_website_supershoes.service;

import org.example.datn_website_supershoes.Enum.Status;
import org.example.datn_website_supershoes.dto.response.BillResponse;
import org.example.datn_website_supershoes.model.Bill;
import org.example.datn_website_supershoes.repository.BillRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
        return billRepository.listBillResponseByStatus(Status.ACTIVE.toString());
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
}
