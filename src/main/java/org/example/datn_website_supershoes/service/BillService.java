package org.example.datn_website_supershoes.service;

import jakarta.persistence.EntityNotFoundException;
import org.example.datn_website_supershoes.Enum.Status;
import org.example.datn_website_supershoes.dto.request.BillRequest;
import org.example.datn_website_supershoes.dto.response.BillResponse;
import org.example.datn_website_supershoes.dto.response.BillStatisticalPieResponse;
import org.example.datn_website_supershoes.dto.response.BillSummaryResponse;
import org.example.datn_website_supershoes.model.Bill;
import org.example.datn_website_supershoes.model.BillDetail;
import org.example.datn_website_supershoes.model.ProductDetail;
import org.example.datn_website_supershoes.repository.BillDetailRepository;
import org.example.datn_website_supershoes.repository.BillRepository;
import org.example.datn_website_supershoes.repository.ProductDetailRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class BillService {

    @Autowired
     BillRepository billRepository;
    @Autowired
     BillDetailRepository billDetailRepository;
    @Autowired
     ProductDetailRepository productDetailRepository;

    public List<BillStatisticalPieResponse> getCompletedBillStatistics() {
        List<Object[]> results = billRepository.findCompletedBillStatisticsByYear();
        List<BillStatisticalPieResponse> statistics = new ArrayList<>();

        for (Object[] result : results) {
            BillStatisticalPieResponse response = new BillStatisticalPieResponse(
                    (Date) result[0], // Assumes the first column is a Date
                    ((Number) result[1]).intValue(), // Assumes the second column is a number of bills
                    (BigDecimal) result[2] ,
                    (String) result[3]// Assumes the third column is the price
            );
            statistics.add(response);
        }
        return statistics;
    }

    @Transactional
    public void updateBillStatusAndNoteByCode(String codeBill, String status, String note) {

        Bill bill = billRepository.findByCodeBill(codeBill)
                .orElseThrow(() -> new RuntimeException("Bill not found with code: " + codeBill));
        //Cập nhật số lượng
        updateQuantityProductDetail(bill.getId(),bill.getStatus(),status);
        bill.setStatus(status);
        bill.setNote(note);

        billRepository.save(bill);
        //Trả số lượng sản phẩm
    }
    @Transactional
    public void updateBillStatus(String codeBill) {
        Bill bill = billRepository.findByCodeBill(codeBill)
                .orElseThrow(() -> new RuntimeException("Bill not found"));

        // Assuming you have some logic for updating status
        if (bill.getStatus().equals("PENDING")) {
            bill.setStatus("CONFIRMED");
            //Cập nhật số lượng
            updateQuantityProductDetail(bill.getId(),"PENDING","CONFIRMED");
        } else if (bill.getStatus().equals("CONFIRMED")) {
            bill.setStatus("WAITTING_FOR_SHIPPED");
        } else if (bill.getStatus().equals("WAITTING_FOR_SHIPPED")) {
            bill.setStatus("SHIPPED");}
        else if (bill.getStatus().equals("SHIPPED")) {
            bill.setStatus("COMPLETED");
        } else {
            throw new RuntimeException("Invalid bill status");
        }

        billRepository.save(bill);
    }


    public Bill createBill(Bill bill) {
        return billRepository.save(bill);
    }


    public Page<BillResponse> getBills(Specification<Bill> spec, Pageable pageable) {
        return billRepository.findAll(spec, pageable).map(this::convertToBillResponse);
    }

    // Update an existing Bill
    public Bill updateBill(Long id, Bill bill) {
        Bill existingBill = billRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bill not found"));


        String[] ignoredProperties = {"id", "createdAt", "createdBy"};
        BeanUtils.copyProperties(bill, existingBill, ignoredProperties);


        if (bill.getVoucher() != null) {
            existingBill.setVoucher(bill.getVoucher());
        }
        if (bill.getCustomer() != null) {
            existingBill.setCustomer(bill.getCustomer());
        }
        if (bill.getEmployees() != null) {
            existingBill.setEmployees(bill.getEmployees());
        }

        if (bill.getPayBills() != null) {
            existingBill.setPayBills(bill.getPayBills());
        }
        if (bill.getBillHistories() != null) {
            existingBill.setBillHistories(bill.getBillHistories());
        }
        if (bill.getBillDetails() != null) {
            existingBill.setBillDetails(bill.getBillDetails());
        }

        return billRepository.save(existingBill);
    }


    public void deleteBill(Long id) {
        billRepository.deleteById(id);
    }


    public Optional<BillResponse> getBillByCodeBill(String codeBill) {
        Optional<Bill> optionalBill = billRepository.findByCodeBill(codeBill);
        return optionalBill.map(this::convertToBillResponse);
    }

    private BillResponse convertToBillResponse(Bill bill) {
        BillResponse response = new BillResponse();
        response.setId(bill.getId());
        response.setCodeBill(bill.getCodeBill());

        if (bill.getNameCustomer() != null) {
            response.setNameCustomer(bill.getNameCustomer());
        }

        response.setPhoneNumber(bill.getPhoneNumber());
        response.setAddress(bill.getAddress());
        response.setNote(bill.getNote());
        response.setType(bill.getType());
        response.setDeliveryDate(bill.getCreatedAt());
        response.setReceiveDate(bill.getCreatedAt());
        response.setTotalMerchandise(bill.getTotalMerchandise());
        response.setPriceDiscount(bill.getPriceDiscount());
        response.setTotalAmount(bill.getTotalAmount());
        response.setStatus(bill.getStatus());
        response.setCreatedAt(bill.getCreatedAt());

        // Copy related object data if present

        if (bill.getVoucher() != null) {
            response.setIdVoucher(bill.getVoucher().getId());
            response.setNameVoucher(bill.getVoucher().getName());
        }
        if (bill.getCustomer() != null) {
            response.setIdCustomer(bill.getCustomer().getId());
        }
        if (bill.getEmployees() != null) {
            response.setIdEmployees(bill.getEmployees().getId());
            response.setNameEmployees(bill.getEmployees().getName());
        }

        return response;
    }

    public Page<BillSummaryResponse> getBillSummaries(Specification<Bill> spec, Pageable pageable) {
        return billRepository.findAll(spec, pageable).map(this::convertToBillSummaryResponse);
    }


    private BillSummaryResponse convertToBillSummaryResponse(Bill bill) {
        return new BillSummaryResponse(
                bill.getCodeBill(),
                bill.getStatus(),
                bill.getNameCustomer(),
                bill.getAddress(),
                bill.getPhoneNumber(),
                bill.getNote()
        );
    }

    public Bill findBillByCode(String codeBill) {
        return billRepository.findByCodeBill(codeBill)
                .orElseThrow(() -> new EntityNotFoundException("Bill not found with code: " + codeBill));
    }

    public Bill save(Bill bill) {
        return billRepository.save(bill);
    }
    @Transactional
    public void updateQuantityProductDetail (Long idBill, String statusNow , String statusNew ){
        //Tìm kiếm list hóa đơn chi tiết
        List<BillDetail> billDetailList = billDetailRepository.findByIdBill(idBill);
        for (BillDetail billDetail : billDetailList){
            //Tìm kiếm sản phẩm chi tiết theo id và trạng thái
            Optional<ProductDetail> productDetailOptional = productDetailRepository.findById(billDetail.getProductDetail().getId());
            // Kiểm tra sản phẩm có tồn tại không
            if (!productDetailOptional.isPresent()) {
                throw new RuntimeException("Tài nguyên sản phẩm không tồn tại trong hệ thống.");
            }
            ProductDetail productDetail = productDetailOptional.get();
            //Số lượng sản phẩm của sản phẩm chi tiết
            int quantityProductDetail = productDetail.getQuantity();

            if(statusNew.equals("CONFIRMED")){
                if(statusNow.equals("PENDING")){
                    if (!productDetailOptional.get().getStatus().equals(Status.ACTIVE.toString())) {
                        throw new RuntimeException("Sản phẩm " + productDetailOptional.get().getProduct().getName() + " đã dừng bán.");
                    }
                    // Kiểm tra số lượng sản phẩm số lượng mua so với sản phẩm còn lại trong kho
                    if (billDetail.getQuantity() > quantityProductDetail || quantityProductDetail <= 0) {
                        throw new RuntimeException("Sản phẩm " + productDetailOptional.get().getProduct().getName() + " không đủ số lượng.");
                    }
                    //Trừ số lượng sản phẩm
                    quantityProductDetail = quantityProductDetail - billDetail.getQuantity();
                    if (quantityProductDetail <= 0) {
                        productDetailOptional.get().setStatus(Status.INACTIVE.toString());
                    }
                    productDetail.setQuantity(quantityProductDetail);
                }else{
                    throw new RuntimeException("Có lỗi xảy ra khi cập nhật trạng thái hóa đơn");
                }
            } else if (statusNew.equals("CANCELLED")) {
                if(!statusNow.equals("PENDING")){
                    //Cộng số lượng sản phẩm
                    quantityProductDetail = quantityProductDetail + billDetail.getQuantity();
                    productDetail.setQuantity(quantityProductDetail);
                }
            }
            //Cập nhật lại sản phẩm
            productDetailRepository.save(productDetail);
        }
    }
}
