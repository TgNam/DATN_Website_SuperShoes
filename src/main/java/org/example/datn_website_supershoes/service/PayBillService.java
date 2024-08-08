package org.example.datn_website_supershoes.service;

import org.example.datn_website_supershoes.Enum.Status;
import org.example.datn_website_supershoes.dto.response.PayBillResponse;
import org.example.datn_website_supershoes.model.PayBill;
import org.example.datn_website_supershoes.repository.PayBillRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PayBillService {

    @Autowired
    private PayBillRepository payBillRepository;

    public PayBill createPayBill(PayBill payBill) {
        return payBillRepository.save(payBill);
    }

    public List<PayBillResponse> getAllPayBills() {
        return payBillRepository.listPayBillResponeByStatus(Status.ACTIVE.toString());
    }

    public Optional<PayBill> getPayBillById(Long id) {
        return payBillRepository.findById(id);
    }

    public PayBill updatePayBill(Long id, PayBill payBill) {
        PayBill existingPayBill = payBillRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("PayBill not found"));

        String[] ignoredProperties = {"id", "createdAt", "createdBy"};
        BeanUtils.copyProperties(payBill, existingPayBill, ignoredProperties);

        // Update associations if needed
        if (payBill.getBill() != null) {
            existingPayBill.setBill(payBill.getBill());
        }
        if (payBill.getPaymentMethod() != null) {
            existingPayBill.setPaymentMethod(payBill.getPaymentMethod());
        }

        return payBillRepository.save(existingPayBill);
    }

    public void deletePayBill(Long id) {
        payBillRepository.deleteById(id);
    }
}
