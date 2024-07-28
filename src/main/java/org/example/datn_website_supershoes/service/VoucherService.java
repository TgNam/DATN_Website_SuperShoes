package org.example.datn_website_supershoes.service;

import org.example.datn_website_supershoes.model.Voucher;
import org.example.datn_website_supershoes.repository.VoucherRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VoucherService {

    @Autowired
    private VoucherRepository voucherRepository;

    public Voucher createVoucher(Voucher voucher) {
        return voucherRepository.save(voucher);
    }

    public List<Voucher> getAllVouchers() {
        return voucherRepository.findAll();
    }

    public Optional<Voucher> getVoucherById(Long id) {
        return voucherRepository.findById(id);
    }

    public Voucher updateVoucher(Long id, Voucher voucherDetail) {
        Voucher voucher = voucherRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Voucher not found"));

        // Loại trừ các thuộc tính không mong muốn
        String[] ignoredProperties = {"id", "createdAt", "createdBy"};
        BeanUtils.copyProperties(voucherDetail, voucher, ignoredProperties);
        return voucherRepository.save(voucher);
    }

    public void deleteVoucher(Long id) {
        voucherRepository.deleteById(id);
    }
}
