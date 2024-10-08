package org.example.datn_website_supershoes.service;

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
import org.springframework.stereotype.Service;

@Service
public class VoucherService {

    @Autowired
    private VoucherRepository voucherRepository;

    public Page<VoucherResponse> getVouchers(Specification<Voucher> spec, Pageable pageable) {
        return voucherRepository.findAll(spec, pageable).map(this::convertToVoucherResponse);
    }

    public Voucher createVoucher(VoucherRequest voucherRequest) {
        Voucher voucher = convertVoucherRequestDTO(voucherRequest);
        voucher.setStatus(Status.ONGOING.toString());
        return voucherRepository.save(voucher);
    }

    public Voucher updateVoucher(Long id, VoucherRequest voucherRequest) {
        Voucher voucher = voucherRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Voucher not found"));

        // Những thuộc tính không được cập nhật
        String[] ignoredProperties = {"id", "createdAt", "createdBy", "status"};
        BeanUtils.copyProperties(voucherRequest, voucher, ignoredProperties);

        // Cập nhật lại dữ liệu trong cơ sở dữ liệu
        return voucherRepository.save(voucher);
    }

    public void deleteVoucher(Long id) {
        Voucher voucher = voucherRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Voucher not found"));
        voucherRepository.delete(voucher);
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
                .type(voucherRequest.getType())
                .minBillValue(voucherRequest.getMinBillValue())
                .startAt(voucherRequest.getStartAt())
                .endAt(voucherRequest.getEndAt())
                .build();
    }
}
