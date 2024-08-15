package org.example.datn_website_supershoes.service;

import org.example.datn_website_supershoes.Enum.Status;
import org.example.datn_website_supershoes.dto.response.BillDetailResponse;
import org.example.datn_website_supershoes.model.BillDetail;
import org.example.datn_website_supershoes.repository.BillDetailRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BillDetailService {

    @Autowired
    private BillDetailRepository billDetailRepository;

    public BillDetail createBillDetail(BillDetail billDetail) {
        return billDetailRepository.save(billDetail);
    }

    public List<BillDetailResponse> getAllBillDetails() {
        return billDetailRepository.listBillResponeByStatus(Status.ACTIVE.toString());
    }

    public Optional<BillDetail> getBillDetailById(Long id) {
        return billDetailRepository.findById(id);
    }

    public BillDetail updateBillDetail(Long id, BillDetail billDetail) {
        BillDetail existingBillDetail = billDetailRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("BillDetail not found"));

        String[] ignoredProperties = {"id", "createdAt", "createdBy"};
        BeanUtils.copyProperties(billDetail, existingBillDetail, ignoredProperties);

        if (billDetail.getBill() != null) {
            existingBillDetail.setBill(billDetail.getBill());
        }
        if (billDetail.getProductDetail() != null) {
            existingBillDetail.setProductDetail(billDetail.getProductDetail());
        }

        return billDetailRepository.save(existingBillDetail);
    }

    public void deleteBillDetail(Long id) {
        billDetailRepository.deleteById(id);
    }
}
