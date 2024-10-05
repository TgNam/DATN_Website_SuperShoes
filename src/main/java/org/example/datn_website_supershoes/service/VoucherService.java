package org.example.datn_website_supershoes.service;

import org.apache.commons.lang3.RandomStringUtils;
import org.example.datn_website_supershoes.Enum.Status;
import org.example.datn_website_supershoes.dto.request.VoucherRequest;
import org.example.datn_website_supershoes.dto.response.VoucherResponse;
import org.example.datn_website_supershoes.model.Account;
import org.example.datn_website_supershoes.model.Voucher;
import org.example.datn_website_supershoes.repository.AccountRepository;
import org.example.datn_website_supershoes.repository.VoucherRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class VoucherService {

    @Autowired
    private VoucherRepository voucherRepository;

    @Autowired
    private AccountRepository accountRepository;

    private String generateVoucherCode() {
        int length = 6 + (int) (Math.random() * 3);
        String randomAlphanumeric = RandomStringUtils.randomAlphanumeric(length).toUpperCase();
        return randomAlphanumeric;
    }

    public long countVouchers(Specification<Voucher> spec) {
        return voucherRepository.count(spec);
    }

    public Voucher createVoucher(VoucherRequest voucherRequest, Long userId) {
        Voucher voucher = convertVoucherRequestDTO(voucherRequest);
        voucher.setCodeVoucher(generateVoucherCode());
        Date currentDate = new Date();

        Account creator = accountRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User không tồn tại"));

        voucher.setCreatedBy(creator.getName());

        if (voucher.getStartAt().after(currentDate)) {
            voucher.setStatus(Status.UPCOMING.toString());
        } else if (voucher.getStartAt().before(currentDate) && voucher.getEndAt().after(currentDate)) {
            voucher.setStatus(Status.ONGOING.toString());
        }

        return voucherRepository.save(voucher);
    }

    public Voucher updateVoucher(Long id, VoucherRequest voucherRequest, Long userId) {
        Voucher voucher = voucherRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Voucher không tồn tại"));
        Date currentDate = new Date();

        if (voucher.getStatus().equals(Status.EXPIRED.toString())) {
            throw new RuntimeException("Không thể cập nhật voucher đã hết hạn.");
        }

        Account updater = accountRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User không tồn tại"));

        String[] ignoredProperties = {"id", "createdAt", "createdBy", "status", "codeVoucher"};
        BeanUtils.copyProperties(voucherRequest, voucher, ignoredProperties);
        voucher.setUpdatedBy(updater.getName());

        return voucherRepository.save(voucher);
    }


    public void deleteVoucher(Long id) {
        Voucher voucher = voucherRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Voucher không tồn tại"));
        voucherRepository.delete(voucher);
    }

    public Voucher endVoucherEarly(Long id, Long userId) {
        Voucher voucher = voucherRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Voucher không tồn tại"));

        Account updater = accountRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User không tồn tại"));

        voucher.setStatus(Status.ENDED_EARLY.toString());
        voucher.setUpdatedBy(updater.getName());

        return voucherRepository.save(voucher);
    }

    public Voucher reactivateVoucher(Long id, Long userId) {
        Voucher voucher = voucherRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Voucher không tồn tại"));

        if (!voucher.getStatus().equals(Status.ENDED_EARLY.toString())) {
            throw new RuntimeException("Chỉ có thể bật lại voucher có trạng thái 'Kết thúc sớm'.");
        }

        Account updater = accountRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User không tồn tại"));

        Date currentDate = new Date();
        if (voucher.getEndAt().after(currentDate)) {
            if (voucher.getStartAt().after(currentDate)) {
                voucher.setStatus(Status.UPCOMING.toString());
            } else {
                voucher.setStatus(Status.ONGOING.toString());
            }
        } else {
            throw new RuntimeException("Không thể bật lại voucher đã quá ngày hết hạn.");
        }

        voucher.setUpdatedBy(updater.getName());
        return voucherRepository.save(voucher);
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void checkAndExpireVouchers() {
        List<Voucher> vouchers = voucherRepository.findAll();
        Date currentDate = new Date();

        for (Voucher voucher : vouchers) {
            if (voucher.getEndAt().before(currentDate) && !voucher.getStatus().equals(Status.EXPIRED.toString())) {
                voucher.setStatus(Status.EXPIRED.toString());
                voucherRepository.save(voucher);
            }
        }
    }

    private VoucherResponse convertToVoucherResponse(Voucher voucher) {
        VoucherResponse response = new VoucherResponse();
        BeanUtils.copyProperties(voucher, response);
        return response;
    }

    private Voucher convertVoucherRequestDTO(VoucherRequest voucherRequest) {
        return Voucher.builder()
                .codeVoucher(voucherRequest.getCodeVoucher())
                .name(voucherRequest.getName())
                .note(voucherRequest.getNote())
                .value(voucherRequest.getValue())
                .quantity(voucherRequest.getQuantity())
                .maximumDiscount(voucherRequest.getMaximumDiscount())
                .type(voucherRequest.getType())  // Kiểu giảm giá (0 = % , 1 = tiền)
                .minBillValue(voucherRequest.getMinBillValue())
                .isPrivate(voucherRequest.getIsPrivate()) // Loại voucher (true = riêng tư, false = công khai)
                .startAt(voucherRequest.getStartAt())
                .endAt(voucherRequest.getEndAt())
                .build();
    }

    public Page<VoucherResponse> getVouchers(Specification<Voucher> spec, Pageable pageable) {
        return voucherRepository.findAll(spec, pageable).map(this::convertToVoucherResponse);
    }
}
