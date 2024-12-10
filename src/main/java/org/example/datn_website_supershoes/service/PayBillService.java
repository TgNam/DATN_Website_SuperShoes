package org.example.datn_website_supershoes.service;

import jakarta.transaction.Transactional;
import org.example.datn_website_supershoes.dto.request.PayBillRequest;
import org.example.datn_website_supershoes.dto.response.PayBillOrderResponse;
import org.example.datn_website_supershoes.dto.response.PayBillResponse;
import org.example.datn_website_supershoes.model.Account;
import org.example.datn_website_supershoes.model.Bill;
import org.example.datn_website_supershoes.model.PayBill;
import org.example.datn_website_supershoes.model.PaymentMethod;
import org.example.datn_website_supershoes.repository.BillRepository;
import org.example.datn_website_supershoes.repository.PayBillRepository;
import org.example.datn_website_supershoes.repository.PaymentMethodRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PayBillService {
    @Autowired
     BillRepository billRepository;
    @Autowired
     PaymentMethodRepository paymentMethodRepository;
    @Autowired
    PayBillRepository payBillRepository;
    public Page<PayBillResponse> getPayBills(Specification<PayBill> spec, Pageable pageable) {
        return payBillRepository.findAll(spec, pageable).map(this::convertToPayBillResponse);
    }

    public void updatePaymentBill(String codeBill, String status) {
        // Fetch all PayBills associated with the given codeBill
        List<PayBill> payBills = payBillRepository.findByBillCodeBill(codeBill);

        if (payBills.isEmpty()) {
            throw new RuntimeException("Bill not found with code: " + codeBill);
        }

        // Update the status for each PayBill
        payBills.forEach(payBill -> {
            payBill.setStatus(status);
            payBillRepository.save(payBill);
        });
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


    //by Nam
    public PayBill createPayBill(PayBillRequest payBillRequest, Integer type, String status) {
        Optional<Bill> billOptional = billRepository.findByCodeBill(payBillRequest.getCodeBill());
        if (billOptional.isEmpty()) {
            throw new RuntimeException("Hóa đơn không tồn tại");
        }
        Optional<PaymentMethod> optionalPaymentMethod = paymentMethodRepository.findByType(payBillRequest.getType());
        if (optionalPaymentMethod.isEmpty()) {
            throw new RuntimeException("Phương thức thanh toán không tồn tại");
        }
        UUID uuid = UUID.randomUUID();
        PayBill payBill = PayBill.builder()
                .amount(payBillRequest.getAmount())
                .bill(billOptional.get())
                .paymentMethod(optionalPaymentMethod.get())
                .type(type)
                .tradingCode("PD-" + uuid)
                .build();
        payBill.setStatus(status);
        return payBillRepository.save(payBill);
    }

    public void deletePayBillById(Long id) {
        Optional<PayBill> optionalPayBill = payBillRepository.findById(id);
        if (optionalPayBill.isEmpty()) {
            throw new RuntimeException("Phương thức thanh toán không tồn tại");
        }
        payBillRepository.delete(optionalPayBill.get());
    }

    public List<PayBillOrderResponse> findPayBillOrderByCodeBill(String codeBill) {
        return payBillRepository.findByCodeBill(codeBill);
    }
}
