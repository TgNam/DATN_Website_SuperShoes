package org.example.datn_website_supershoes.service;

import org.example.datn_website_supershoes.Enum.Status;
import org.example.datn_website_supershoes.dto.request.AccountVoucherRequest;
import org.example.datn_website_supershoes.model.AccountVoucher;
import org.example.datn_website_supershoes.repository.AccountVoucherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountVoucherService {

    @Autowired
    private AccountVoucherRepository accountVoucherRepository;

    public List<AccountVoucherRequest> getAllAccountVouchers() {
        return accountVoucherRepository.findAccountVoucherRequestsByStatus(Status.ACTIVE.toString());
    }

    public AccountVoucher createAccountVoucher(AccountVoucherRequest accountVoucherRequest) {
        AccountVoucher accountVoucher = convertAccountVoucherRequestDTO(accountVoucherRequest);
        return accountVoucherRepository.save(accountVoucher);
    }

    public AccountVoucher updateAccountVoucher(Long id, AccountVoucherRequest accountVoucherRequest) {
        AccountVoucher accountVoucher = accountVoucherRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("AccountVoucher not found"));

        accountVoucher.setDateOfUse(accountVoucherRequest.getDateOfUse());
        return accountVoucherRepository.save(accountVoucher);
    }

    public void deleteAccountVoucher(Long id) {
        AccountVoucher accountVoucher = accountVoucherRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("AccountVoucher not found"));
        accountVoucherRepository.delete(accountVoucher);
    }

    private AccountVoucher convertAccountVoucherRequestDTO(AccountVoucherRequest accountVoucherRequest) {
        return AccountVoucher.builder()
                .dateOfUse(accountVoucherRequest.getDateOfUse())
                .build();
    }
}
