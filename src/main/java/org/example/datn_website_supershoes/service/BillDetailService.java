package org.example.datn_website_supershoes.service;

import org.example.datn_website_supershoes.dto.request.BillDetailRequest;
import org.example.datn_website_supershoes.dto.response.BillDetailResponse;
import org.example.datn_website_supershoes.model.Bill;
import org.example.datn_website_supershoes.model.BillDetail;
import org.example.datn_website_supershoes.model.Color;
import org.example.datn_website_supershoes.model.Product;
import org.example.datn_website_supershoes.model.ProductDetail;
import org.example.datn_website_supershoes.repository.BillDetailRepository;
import org.example.datn_website_supershoes.repository.BillRepository;
import org.example.datn_website_supershoes.repository.ProductDetailRepository;
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

    @Autowired
    private BillRepository billRepository;

    @Autowired
    private ProductDetailRepository productDetailRepository;

    // Method to create a new BillDetail using BillDetailRequest
    public BillDetail createBillDetail(BillDetailRequest billDetailRequest) {
        BillDetail billDetail = new BillDetail();
        billDetail.setQuantity(billDetailRequest.getQuantity());
        billDetail.setPriceDiscount(billDetailRequest.getPriceDiscount());
        billDetail.setNote(billDetailRequest.getNote());
        billDetail.setStatus(billDetailRequest.getStatus());

        // Set relationships by fetching from the repository
        if (billDetailRequest.getIdBill() != null) {
            Bill bill = billRepository.findById(billDetailRequest.getIdBill())
                    .orElseThrow(() -> new RuntimeException("Bill not found with id: " + billDetailRequest.getIdBill()));
            billDetail.setBill(bill);
        }

        if (billDetailRequest.getIdProductDetail() != null) {
            ProductDetail productDetail = productDetailRepository.findById(billDetailRequest.getIdProductDetail())
                    .orElseThrow(() -> new RuntimeException("ProductDetail not found with id: " + billDetailRequest.getIdProductDetail()));
            billDetail.setProductDetail(productDetail);
        }

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

    public BillDetail updateBillDetail(BillDetailRequest billDetailRequest) {
        // Check if a BillDetail with the given idBill and idProductDetail already exists
        Optional<BillDetail> existingProductDetail = billDetailRepository.findAll().stream()
                .filter(bd -> {
                    if (bd.getProductDetail() != null && bd.getBill() != null) {
                        return bd.getProductDetail().getId().equals(billDetailRequest.getIdProductDetail()) &&
                                bd.getBill().getId().equals(billDetailRequest.getIdBill());
                    }
                    return false;
                })
                .findFirst();

        if (existingProductDetail.isPresent()) {
            // If it exists, update the existing BillDetail's quantity
            BillDetail billDetail = existingProductDetail.get();
            billDetail.setQuantity(billDetail.getQuantity() + billDetailRequest.getQuantity());

            // Update other fields if needed
            if (billDetailRequest.getPriceDiscount() != null) {
                billDetail.setPriceDiscount(billDetailRequest.getPriceDiscount());
            }
            if (billDetailRequest.getNote() != null) {
                billDetail.setNote(billDetailRequest.getNote());
            }
            if (billDetailRequest.getStatus() != null) {
                billDetail.setStatus(billDetailRequest.getStatus());
            }

            return billDetailRepository.save(billDetail); // Save the updated BillDetail
        } else {
            // If it does not exist, create a new BillDetail
            BillDetail newBillDetail = new BillDetail();
            newBillDetail.setQuantity(billDetailRequest.getQuantity());
            newBillDetail.setPriceDiscount(billDetailRequest.getPriceDiscount());
            newBillDetail.setNote(billDetailRequest.getNote());
            newBillDetail.setStatus(billDetailRequest.getStatus());

            // Set relationships
            if (billDetailRequest.getIdBill() != null) {
                Bill bill = billRepository.findById(billDetailRequest.getIdBill())
                        .orElseThrow(() -> new RuntimeException("Bill not found with id: " + billDetailRequest.getIdBill()));
                newBillDetail.setBill(bill);
            }

            if (billDetailRequest.getIdProductDetail() != null) {
                ProductDetail productDetail = productDetailRepository.findById(billDetailRequest.getIdProductDetail())
                        .orElseThrow(() -> new RuntimeException("ProductDetail not found with id: " + billDetailRequest.getIdProductDetail()));
                newBillDetail.setProductDetail(productDetail);
            }

            return billDetailRepository.save(newBillDetail); // Save the new BillDetail
        }
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
    private BillDetailResponse convertToBillDetailResponse(BillDetail billDetail) {
        BillDetailResponse response = new BillDetailResponse();

        response.setId(billDetail.getId());
        response.setQuantity(billDetail.getQuantity());
        response.setStatus(billDetail.getStatus());
        response.setPriceDiscount(billDetail.getPriceDiscount());

        Optional.ofNullable(billDetail.getProductDetail()).ifPresent(productDetail -> {
            Optional.ofNullable(productDetail.getProduct()).ifPresent(product -> {
                // Calculate the total amount using BigDecimal
                BigDecimal totalAmount = productDetail.getPrice().multiply(BigDecimal.valueOf(billDetail.getQuantity()));
                response.setTotalAmount(totalAmount);
               // Set the sumPrice with totalAmount

                response.setImageByte(product.getImageByte());
                response.setNameProduct(product.getName());
                response.setProductCode(product.getProductCode());
            });

            Optional.ofNullable(productDetail.getColor()).ifPresent(color -> {
                response.setNameColor(color.getName());
            });
        });

        return response;
    }



}
