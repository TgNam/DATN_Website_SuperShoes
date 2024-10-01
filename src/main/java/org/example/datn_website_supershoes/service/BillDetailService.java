package org.example.datn_website_supershoes.service;

import org.example.datn_website_supershoes.dto.response.BillDetailResponse;
import org.example.datn_website_supershoes.model.Bill;
import org.example.datn_website_supershoes.model.BillDetail;
import org.example.datn_website_supershoes.model.Color;
import org.example.datn_website_supershoes.model.Product;
import org.example.datn_website_supershoes.model.ProductDetail;
import org.example.datn_website_supershoes.repository.BillDetailRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class BillDetailService {

    @Autowired
    private BillDetailRepository billDetailRepository;

    // Method to create a new BillDetail
    public BillDetail createBillDetail(BillDetail billDetail) {
        return billDetailRepository.save(billDetail);
    }

    // Fetch all BillDetailResponse objects by Bill code
    public List<BillDetailResponse> getBillDetailsByCodeBill(String codeBill) {
        return billDetailRepository.listBillDetailResponseByCodeBill(codeBill);
    }

    // Paginated fetching of BillDetails using Specification for filtering
    public Page<BillDetailResponse> getBillDetails(Specification<BillDetail> spec, Pageable pageable) {
        return billDetailRepository.findAll(spec, pageable).map(this::convertToBillDetailResponse);
    }

    // Fetch all BillDetail entities
    public List<BillDetail> getAllBillDetails() {
        return billDetailRepository.findAll();
    }

    // Fetch BillDetail by ID
    public Optional<BillDetail> getBillDetailById(Long id) {
        return billDetailRepository.findById(id);
    }

    // Update an existing BillDetail
    public BillDetail updateBillDetail(Long id, BillDetail billDetail) {
        BillDetail existingBillDetail = billDetailRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("BillDetail not found"));

        // List of properties to ignore during update
        String[] ignoredProperties = {"id", "createdAt", "createdBy"};
        BeanUtils.copyProperties(billDetail, existingBillDetail, ignoredProperties);

        // Set related entities if they are not null
        if (billDetail.getBill() != null) {
            existingBillDetail.setBill(billDetail.getBill());
        }
        if (billDetail.getProductDetail() != null) {
            existingBillDetail.setProductDetail(billDetail.getProductDetail());
        }

        return billDetailRepository.save(existingBillDetail);
    }

    // Delete a BillDetail by ID
    public void deleteBillDetail(Long id) {
        billDetailRepository.deleteById(id);
    }

    public void deleteBillDetailsByProductCode(String productCode) {
        if (productCode == null || productCode.isEmpty()) {
            throw new IllegalArgumentException("Product code must not be null or empty");
        }

        try {
            // Use the deleteByProductCode method to directly delete the entries
            billDetailRepository.deleteByProductCode(productCode);
        } catch (RuntimeException e) {
            throw new RuntimeException("No BillDetails found for the given product code: " + productCode);
        }
    }


    // Helper method to convert BillDetail entity to BillDetailResponse DTO
    private BillDetailResponse convertToBillDetailResponse(BillDetail billDetail) {
        BillDetailResponse response = new BillDetailResponse();

        // Copy simple attributes
        response.setId(billDetail.getId());
        response.setQuantity(billDetail.getQuantity());
        response.setStatus(billDetail.getStatus()); // Assuming status should reflect a state like "Pending", "Paid", etc.

        // Copy Bill related data if present
        Bill bill = billDetail.getBill(); // Assuming there's a getBill() method in BillDetail
        if (bill != null) {
            BigDecimal totalAmount = bill.getTotalAmount();
            if (totalAmount != null) {
                response.setTotalAmount(totalAmount); // Ensure response has a proper field to handle totalAmount
            }
        }

        // Copy related object data if present
        ProductDetail productDetail = billDetail.getProductDetail();
        if (productDetail != null) {
            Product product = productDetail.getProduct();
            if (product != null) {
                response.setImageByte(product.getImageByte());
                response.setNameProduct(product.getName());
                response.setProductCode(product.getProductCode());
            }

            Color color = productDetail.getColor();
            if (color != null) {
                response.setNameColor(color.getName());
            }
        }

        return response;
    }


}
