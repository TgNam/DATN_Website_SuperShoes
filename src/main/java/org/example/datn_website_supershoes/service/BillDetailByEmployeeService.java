package org.example.datn_website_supershoes.service;

import org.example.datn_website_supershoes.dto.response.BillDetailOrderResponse;
import org.example.datn_website_supershoes.repository.BillDetailByEmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BillDetailByEmployeeService {
    @Autowired
    BillDetailByEmployeeRepository billDetailByEmployeeRepository;

    public List<BillDetailOrderResponse> getBillDetailsByCodeBill(String codeBill){
        return billDetailByEmployeeRepository.getBillDetailsByCodeBill(codeBill);
    }
}
