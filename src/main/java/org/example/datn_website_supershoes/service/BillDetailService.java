package org.example.datn_website_supershoes.service;

import jakarta.transaction.Transactional;
import org.example.datn_website_supershoes.dto.request.BillDetailRequest;
import org.example.datn_website_supershoes.dto.response.BillDetailResponse;
import org.example.datn_website_supershoes.model.Bill;
import org.example.datn_website_supershoes.model.BillDetail;
import org.example.datn_website_supershoes.model.ProductDetail;
import org.example.datn_website_supershoes.repository.BillDetailRepository;
import org.example.datn_website_supershoes.repository.BillRepository;
import org.example.datn_website_supershoes.repository.ProductDetailRepository;
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
    @Transactional
    public BillDetail createBillDetail(BillDetailRequest billDetailRequest) {
        // Check if BillDetail with the same idProductDetail and idBill already exists
        Optional<BillDetail> existingBillDetail = billDetailRepository.findByIdBillAndIdProductDetail(
                billDetailRequest.getIdBill(), billDetailRequest.getIdProductDetail());

        if (existingBillDetail.isPresent()) {
            throw new RuntimeException("BillDetail with this product and bill already exists.");
        }

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

            // Validate product stock and reduce the quantity accordingly
            int updatedProductQuantity = productDetail.getQuantity() - billDetailRequest.getQuantity();
            if (updatedProductQuantity < 0) {
                throw new RuntimeException("Insufficient stock for product: " + productDetail.getProduct().getName());
            }
            productDetail.setQuantity(updatedProductQuantity);
            productDetailRepository.save(productDetail);

            billDetail.setProductDetail(productDetail);
        }

        return billDetailRepository.save(billDetail);
    }
    @Transactional
    public BillDetail createOrUpdateBillDetail(BillDetailRequest billDetailRequest) {
        // Check if BillDetail with the same idProductDetail and idBill already exists
        Optional<BillDetail> existingBillDetail = billDetailRepository.findByIdBillAndIdProductDetail(
                billDetailRequest.getIdBill(), billDetailRequest.getIdProductDetail());

        if (existingBillDetail.isPresent()) {
            // If it exists, update the existing BillDetail
            BillDetail billDetail = existingBillDetail.get();
            billDetail.setQuantity(billDetail.getQuantity() + billDetailRequest.getQuantity());
            if (billDetailRequest.getPriceDiscount() != null) {
                billDetail.setPriceDiscount(billDetailRequest.getPriceDiscount());
            }
            if (billDetailRequest.getNote() != null) {
                billDetail.setNote(billDetailRequest.getNote());
            }
            billDetailRepository.save(billDetail);  // Save updated BillDetail
            return billDetail;
        } else {
            // If it doesn't exist, create a new BillDetail
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

                // Validate product stock and reduce the quantity accordingly
                int updatedProductQuantity = productDetail.getQuantity() - billDetailRequest.getQuantity();
                if (updatedProductQuantity < 0) {
                    throw new RuntimeException("Insufficient stock for product: " + productDetail.getProduct().getName());
                }
                productDetail.setQuantity(updatedProductQuantity);
                productDetailRepository.save(productDetail);  // Save updated ProductDetail

                billDetail.setProductDetail(productDetail);
            }

            return billDetailRepository.save(billDetail);  // Save new BillDetail
        }
    }
    @Transactional
    public BillDetail updateBillDetail(BillDetailRequest billDetailRequest) {
        // Check if the BillDetail exists
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
            BillDetail billDetail = existingProductDetail.get();
            ProductDetail productDetail = billDetail.getProductDetail();  // Get the associated ProductDetail

            // The new quantity to be added
            int quantityToAdd = billDetailRequest.getQuantity();

            // Check if there is enough stock in ProductDetail to accommodate the new quantity
            if (productDetail.getQuantity() < quantityToAdd) {
                throw new RuntimeException("Insufficient stock for product: " + productDetail.getProduct().getName());
            }

            // Update the BillDetail quantity by adding the new quantity
            int updatedBillDetailQuantity = billDetail.getQuantity() + quantityToAdd;
            billDetail.setQuantity(updatedBillDetailQuantity);

            // Decrease the ProductDetail stock by the new quantity added to the BillDetail
            int updatedProductQuantity = productDetail.getQuantity() - quantityToAdd;
            productDetail.setQuantity(updatedProductQuantity);

            // Save the updated ProductDetail
            productDetailRepository.save(productDetail);

            // Update other fields if necessary
            if (billDetailRequest.getPriceDiscount() != null) {
                billDetail.setPriceDiscount(billDetailRequest.getPriceDiscount());
            }
            if (billDetailRequest.getNote() != null) {
                billDetail.setNote(billDetailRequest.getNote());
            }
            if (billDetailRequest.getStatus() != null) {
                billDetail.setStatus(billDetailRequest.getStatus());
            }

            // Save the updated BillDetail
            return billDetailRepository.save(billDetail);

        } else {
            throw new RuntimeException("BillDetail not found with the given criteria");
        }
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

    // Delete a BillDetail by ID
    public void deleteBillDetail(Long id) {
        billDetailRepository.deleteById(id);
    }

    // Delete BillDetails by product code
    @Transactional
    public void deleteBillDetailAndUpdateProduct(String productCode, String nameColor,String nameSize) {
        // Find the BillDetail by productCode and nameColor
        List<BillDetail> billDetails = billDetailRepository.findByProductCodeAndColorName(productCode, nameColor,nameSize);
        if (billDetails.isEmpty()) {
            throw new RuntimeException("No BillDetail found with the given product code and color.");
        }

        // Process each BillDetail
        for (BillDetail billDetail : billDetails) {
            ProductDetail productDetail = billDetail.getProductDetail();
            if (productDetail == null) {
                throw new RuntimeException("ProductDetail not found for BillDetail.");
            }

            // Add the BillDetail's quantity back to the ProductDetail
            int updatedProductQuantity = productDetail.getQuantity() + billDetail.getQuantity();
            productDetail.setQuantity(updatedProductQuantity);

            // Save the updated ProductDetail
            productDetailRepository.save(productDetail);

            // Delete the BillDetail
            billDetailRepository.delete(billDetail);
        }
    }


    // Convert BillDetail to BillDetailResponse
    private BillDetailResponse convertToBillDetailResponse(BillDetail billDetail) {
        BillDetailResponse response = new BillDetailResponse();
        response.setId(billDetail.getId());
        response.setQuantity(billDetail.getQuantity());
        response.setStatus(billDetail.getStatus());
        response.setPriceDiscount(billDetail.getPriceDiscount());


        Optional.ofNullable(billDetail.getProductDetail()).ifPresent(productDetail -> {
            Optional.ofNullable(productDetail.getProduct()).ifPresent(product -> {
                // Calculate total amount
                BigDecimal totalAmount = productDetail.getPrice().multiply(BigDecimal.valueOf(billDetail.getQuantity()));
                response.setTotalAmount(totalAmount);

                response.setImageByte(product.getImageByte());
                response.setNameProduct(product.getName());
                response.setProductCode(product.getProductCode());
            });

            Optional.ofNullable(productDetail.getColor()).ifPresent(color -> {
                response.setNameColor(color.getName());
            });
            Optional.ofNullable(productDetail.getSize()).ifPresent(size -> {
                response.setSizeName(size.getName());
            });
        });

        return response;
    }
}
