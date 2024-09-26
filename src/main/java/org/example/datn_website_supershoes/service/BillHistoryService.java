package org.example.datn_website_supershoes.service;

import org.example.datn_website_supershoes.dto.request.BillHistoryRequest;
import org.example.datn_website_supershoes.dto.response.BillHistoryResponse;
import org.example.datn_website_supershoes.model.Bill;
import org.example.datn_website_supershoes.model.Account;
import org.example.datn_website_supershoes.model.BillHistory;
import org.example.datn_website_supershoes.repository.BillHistoryRepository;
import org.example.datn_website_supershoes.repository.BillRepository;
import org.example.datn_website_supershoes.repository.AccountRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BillHistoryService {

    @Autowired
    private BillHistoryRepository billHistoryRepository;

    @Autowired
    private BillRepository billRepository;

    @Autowired
    private AccountRepository accountRepository;

    // Create a new BillHistory entry
    public BillHistoryResponse createBillHistory(BillHistoryRequest billHistoryRequest) {
        BillHistory billHistory = new BillHistory();
        BeanUtils.copyProperties(billHistoryRequest, billHistory);

        // Fetch Bill and Account entities based on the IDs from the request
        Optional<Bill> billOptional = billRepository.findById(billHistoryRequest.getBillId());
        Optional<Account> accountOptional = accountRepository.findById(billHistoryRequest.getAccountId());

        if (billOptional.isEmpty() || accountOptional.isEmpty()) {
            throw new RuntimeException("Bill or Account not found");
        }

        billHistory.setBill(billOptional.get());
        billHistory.setAccount(accountOptional.get());

        BillHistory savedBillHistory = billHistoryRepository.save(billHistory);

        // Convert and return as a BillHistoryResponse
        return convertToResponse(savedBillHistory);
    }

    // Get all BillHistory entries for a specific Bill
    public List<BillHistoryResponse> getBillHistoryByBillId(Long billId) {
        List<BillHistory> billHistories = billHistoryRepository.findBillHistoryByBillId(billId);
        return billHistories.stream().map(this::convertToResponse).collect(Collectors.toList());
    }

    // Get all BillHistory entries for a specific Account
    public List<BillHistoryResponse> getBillHistoryByAccountId(Long accountId) {
        List<BillHistory> billHistories = billHistoryRepository.findBillHistoryByAccountId(accountId);
        return billHistories.stream().map(this::convertToResponse).collect(Collectors.toList());
    }

    // Convert a BillHistory entity to a BillHistoryResponse DTO
    private BillHistoryResponse convertToResponse(BillHistory billHistory) {
        return BillHistoryResponse.builder()
                .id(billHistory.getId())
                .note(billHistory.getNote())
                .idBill(billHistory.getBill().getId())// Assuming Bill has a field 'codeBill'
                .idAccount(billHistory.getAccount().getId())
                .status(billHistory.getBill().getStatus()) // Assuming Bill has a field 'status'
                .build();
    }

    // Delete BillHistory by ID
    public void deleteBillHistory(Long id) {
        billHistoryRepository.deleteById(id);
    }

    // Update an existing BillHistory
    public BillHistoryResponse updateBillHistory(Long id, BillHistoryRequest billHistoryRequest) {
        BillHistory existingBillHistory = billHistoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("BillHistory not found"));

        BeanUtils.copyProperties(billHistoryRequest, existingBillHistory, "id");

        // Update associations if they exist in the request
        Optional<Bill> billOptional = billRepository.findById(billHistoryRequest.getBillId());
        Optional<Account> accountOptional = accountRepository.findById(billHistoryRequest.getAccountId());

        if (billOptional.isEmpty() || accountOptional.isEmpty()) {
            throw new RuntimeException("Bill or Account not found");
        }

        existingBillHistory.setBill(billOptional.get());
        existingBillHistory.setAccount(accountOptional.get());

        BillHistory updatedBillHistory = billHistoryRepository.save(existingBillHistory);

        return convertToResponse(updatedBillHistory);
    }
}
