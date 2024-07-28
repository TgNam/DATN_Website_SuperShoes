package org.example.datn_website_supershoes.service;

import org.example.datn_website_supershoes.model.AccountVoucher;
import org.example.datn_website_supershoes.repository.AccountVoucherRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AccountVoucherService {

    @Autowired
    private AccountVoucherRepository accountVoucherRepository;

    public AccountVoucher createAccountVoucher(AccountVoucher accountVoucher) {
        return accountVoucherRepository.save(accountVoucher);
    }

    public List<AccountVoucher> getAllAccountVouchers() {
        return accountVoucherRepository.findAll();
    }

    public Optional<AccountVoucher> getAccountVoucherById(Long id) {
        return accountVoucherRepository.findById(id);
    }

    public AccountVoucher updateAccountVoucher(Long id, AccountVoucher accountVoucherDetail) {
        AccountVoucher accountVoucher = accountVoucherRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("AccountVoucher not found"));

        String[] ignoredProperties = {"id", "createdAt", "createdBy"};
        BeanUtils.copyProperties(accountVoucherDetail, accountVoucher, ignoredProperties);

        if (accountVoucherDetail.getDateOfUse() != null) {
            accountVoucher.setDateOfUse(accountVoucherDetail.getDateOfUse());
        }
        if (accountVoucherDetail.getAccount() != null) {
            accountVoucher.setAccount(accountVoucherDetail.getAccount());
        }
        if (accountVoucherDetail.getVoucher() != null) {
            accountVoucher.setVoucher(accountVoucherDetail.getVoucher());
        }
        return accountVoucherRepository.save(accountVoucher);
    }

    public void deleteAccountVoucher(Long id) {
        accountVoucherRepository.deleteById(id);
    }
}
