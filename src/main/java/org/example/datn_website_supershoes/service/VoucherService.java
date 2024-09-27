package org.example.datn_website_supershoes.service;

import org.apache.commons.lang3.RandomStringUtils;
import org.example.datn_website_supershoes.Enum.Status;
import org.example.datn_website_supershoes.dto.request.VoucherRequest;
import org.example.datn_website_supershoes.dto.response.VoucherResponse;
import org.example.datn_website_supershoes.model.Voucher;
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

@Service
public class VoucherService {

    @Autowired
    private VoucherRepository voucherRepository;

    private String generateVoucherCode() {
        return RandomStringUtils.randomAlphabetic(6, 8).toUpperCase();
    }

    public Voucher createVoucher(VoucherRequest voucherRequest) {
        Voucher voucher = convertVoucherRequestDTO(voucherRequest);
        voucher.setCodeVoucher(generateVoucherCode());
        Date currentDate = new Date();

        if (voucher.getStartAt().after(currentDate)) {
            voucher.setStatus(Status.UPCOMING.toString());
        }
        else if (voucher.getStartAt().before(currentDate) && voucher.getEndAt().after(currentDate)) {
            voucher.setStatus(Status.ONGOING.toString());
        }

        return voucherRepository.save(voucher);
    }


    public Voucher updateVoucher(Long id, VoucherRequest voucherRequest) {
        Voucher voucher = voucherRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Voucher không tồn tại"));
        Date currentDate = new Date();

        if (voucher.getStatus().equals(Status.ENDING_SOON.toString()) ||
                voucher.getStatus().equals(Status.EXPIRED.toString())) {
            throw new RuntimeException("Không thể cập nhật voucher đã hết hạn.");
        }

        String[] ignoredProperties = {"id", "createdAt", "createdBy", "status", "codeVoucher"};
        BeanUtils.copyProperties(voucherRequest, voucher, ignoredProperties);

        return voucherRepository.save(voucher);
    }


    public void deleteVoucher(Long id) {
        Voucher voucher = voucherRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Voucher không tồn tại"));
        voucherRepository.delete(voucher);
    }

    public Voucher endVoucherEarly(Long id, String updatedBy) {
        Voucher voucher = voucherRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Voucher không tồn tại"));

        voucher.setStatus(Status.ENDING_SOON.toString());
        voucher.setUpdatedBy(updatedBy); 

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
