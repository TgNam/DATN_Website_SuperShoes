package org.example.datn_website_supershoes.service;

import org.example.datn_website_supershoes.dto.response.PayBillResponse;
import org.example.datn_website_supershoes.model.Account;
import org.example.datn_website_supershoes.model.PayBill;
import org.example.datn_website_supershoes.model.PaymentMethod;
import org.example.datn_website_supershoes.repository.PayBillRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PayBillService {

    private final PayBillRepository payBillRepository;

    // Constructor-based dependency injection
    public PayBillService(PayBillRepository payBillRepository) {
        this.payBillRepository = payBillRepository;
    }

    // Method to create a new PayBill
    public PayBill createPayBill(PayBill payBill) {
        return payBillRepository.save(payBill);
    }

    // Fetch all PayBillResponse objects for a specific bill code
    public List<PayBillResponse> getAllPayBills(String codeBill) {
        return payBillRepository.listPayBillResponseByCodeBill(codeBill);
    }

    // Paginated fetching of pay bills using Specification for filtering
    public Page<PayBillResponse> getPayBills(Specification<PayBill> spec, Pageable pageable) {
        return payBillRepository.findAll(spec, pageable).map(this::convertToPayBillResponse);
    }

    // Fetch all PayBill entities
    public List<PayBill> getAllPayBillEntities() {  // Renamed method for better clarity
        return payBillRepository.findAll();
    }

    // Fetch PayBill by ID
    public Optional<PayBill> getPayBillById(Long id) {
        return payBillRepository.findById(id);
    }

    // Update an existing PayBill
    public PayBill updatePayBill(Long id, PayBill payBill) {
        PayBill existingPayBill = payBillRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("PayBill not found"));  // Consider using a custom exception

        // List of properties to ignore during update
        String[] ignoredProperties = {"id", "createdAt", "createdBy"};
        BeanUtils.copyProperties(payBill, existingPayBill, ignoredProperties);

        // Set related entities if they are not null
        if (payBill.getBill() != null) {
            existingPayBill.setBill(payBill.getBill());
        }
        if (payBill.getPaymentMethod() != null) {
            existingPayBill.setPaymentMethod(payBill.getPaymentMethod());
        }

        return payBillRepository.save(existingPayBill);
    }

    // Delete a PayBill by ID and return whether it was successful
    public boolean deletePayBill(Long id) {
        if (payBillRepository.existsById(id)) {
            payBillRepository.deleteById(id);
            return true;
        } else {
            return false;  // Could also throw a custom exception for better error handling
        }
    }

    // Helper method to convert PayBill entity to PayBillResponse DTO
    private PayBillResponse convertToPayBillResponse(PayBill payBill) {
        PayBillResponse response = new PayBillResponse();

        // Copy simple attributes
        response.setId(payBill.getId());
        response.setAmount(payBill.getAmount());
        response.setTradingCode(payBill.getTradingCode());
        response.setNote(payBill.getNote());
        response.setStatus(payBill.getStatus());
        response.setCreatedAt(payBill.getCreatedAt());
        response.setType(payBill.getType());

        // Copy attributes from PaymentMethod if available
        PaymentMethod paymentMethod = payBill.getPaymentMethod();
        if (paymentMethod != null) {
            response.setNamePayment(paymentMethod.getMethodName());
        }

        // Copy attributes from Account if available
        Account account = payBill.getBill().getEmployees();
        if (account != null) {
            response.setNameEployee(account.getName());
        }

        return response;
    }

}
