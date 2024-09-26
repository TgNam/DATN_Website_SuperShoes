package org.example.datn_website_supershoes.service;

import org.example.datn_website_supershoes.Enum.Status;
import org.example.datn_website_supershoes.dto.request.BillRequest;
import org.example.datn_website_supershoes.dto.request.SizeRequest;
import org.example.datn_website_supershoes.model.Account;
import org.example.datn_website_supershoes.model.Bill;
import org.example.datn_website_supershoes.model.BillDetail;
import org.example.datn_website_supershoes.model.Size;
import org.example.datn_website_supershoes.repository.AccountRepository;
import org.example.datn_website_supershoes.repository.BillByEmployeeRepository;
import org.example.datn_website_supershoes.repository.BillDetailByEmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BillByEmployeeService {

    @Autowired
    BillByEmployeeRepository billByEmployeeRepository;

    @Autowired
    AccountRepository accountRepository;

    public List<String> getListCodeBillWaitingForPayment() {
        Long idEmployees = 2L;
        return billByEmployeeRepository.findCodeBillWaitingForPayment(idEmployees, Status.WAITING_FOR_PAYMENT.toString());
    }

    public Bill createBillByEmployee() {
        Bill bill = billByEmployeeRepository.save(convertBill());
        return bill;
    }

    public static String generateRandomCode() {
        Random random = new Random();
        // Tạo một số ngẫu nhiên có 6 chữ số
        int randomNumber = 100000 + random.nextInt(900000); // Số ngẫu nhiên từ 100000 đến 999999
        // Kết hợp với tiền tố HD
        return "HD" + randomNumber;
    }

    public Bill convertBill() {
        Long id = 2L;
        Account employee = accountRepository.findById(id).get();
        UUID uuid = UUID.randomUUID();
        String generatedCode = this.generateRandomCode() + "-" + uuid;
        Bill bill = Bill.builder()
                .employees(employee)
                .codeBill(generatedCode)
                .type(2)
                .build();
        bill.setCreatedBy(employee.getName());
        bill.setUpdatedBy(employee.getName());
        bill.setStatus(Status.WAITING_FOR_PAYMENT.toString());
        return bill;
    }
}
