package org.example.datn_website_supershoes.service;

import org.example.datn_website_supershoes.Enum.Status;
import org.example.datn_website_supershoes.dto.response.BillDetailOrderResponse;
import org.example.datn_website_supershoes.model.BillDetail;
import org.example.datn_website_supershoes.model.ProductDetail;
import org.example.datn_website_supershoes.repository.BillDetailByEmployeeRepository;
import org.example.datn_website_supershoes.repository.ProductDetailRepository;
import org.example.datn_website_supershoes.webconfig.NotificationController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BillDetailByEmployeeService {
    @Autowired
    private   BillDetailByEmployeeRepository billDetailByEmployeeRepository;
    @Autowired
    private ProductDetailRepository productDetailRepository;
    @Autowired
    private NotificationController notificationController;
    public List<BillDetailOrderResponse> getBillDetailsByCodeBill(String codeBill){
        return billDetailByEmployeeRepository.getBillDetailsByCodeBill(codeBill);
    }
    public BillDetail plusBillDetail(Long idBillDetail, Long idProductDetail){
        Optional<BillDetail> billDetailOptional = billDetailByEmployeeRepository.findById(idBillDetail);

        if(billDetailOptional.isEmpty()){
            throw new RuntimeException("Hóa đơn không tồn tại!");
        }

        Optional<ProductDetail> optionalProductDetail = productDetailRepository.findByIdAndAndStatusAndPrice(idProductDetail, Status.ACTIVE.toString(),billDetailOptional.get().getPriceDiscount());

        if(optionalProductDetail.isEmpty()){
            throw new RuntimeException("Sản phẩm với mức giá "+billDetailOptional.get().getPriceDiscount()+" VND đã hết hàng!");
        }
        if(optionalProductDetail.get().getQuantity() <= 0){
            throw new RuntimeException("Sản phẩm đã hết hàng");
        }
        Integer newQuantity = optionalProductDetail.get().getQuantity() - 1;
        optionalProductDetail.get().setQuantity(newQuantity);
        if(newQuantity <= 0){
            optionalProductDetail.get().setStatus(Status.INACTIVE.toString());
        }
        productDetailRepository.save(optionalProductDetail.get());
        billDetailOptional.get().setQuantity(billDetailOptional.get().getQuantity() + 1);
        BillDetail billDetail = billDetailByEmployeeRepository.save(billDetailOptional.get());
        notificationController.sendNotification();
        return billDetail;
    }

    public BillDetail subtractBillDetail(Long idBillDetail, Long idProductDetail){
        Optional<BillDetail> billDetailOptional = billDetailByEmployeeRepository.findById(idBillDetail);

        if(billDetailOptional.isEmpty()){
            throw new RuntimeException("Hóa đơn không tồn tại!");
        }

        Optional<ProductDetail> optionalProductDetail = productDetailRepository.findById(idProductDetail);

        if(optionalProductDetail.isEmpty()){
            throw new RuntimeException("Sản phẩm không tồn tại!");
        }
        Integer newQuantity = billDetailOptional.get().getQuantity() - 1;

        if(newQuantity <= 0){
            throw new RuntimeException("Tối thiểu phải có 1 sản phẩm");
        }
        billDetailOptional.get().setQuantity(newQuantity);

        optionalProductDetail.get().setQuantity(optionalProductDetail.get().getQuantity() + 1);

        productDetailRepository.save(optionalProductDetail.get());
        BillDetail billDetail = billDetailByEmployeeRepository.save(billDetailOptional.get());
        notificationController.sendNotification();
        return billDetail;
    }

    public void deleteBillDetail(Long idBillDetail, Long idProductDetail){
        Optional<BillDetail> billDetailOptional = billDetailByEmployeeRepository.findById(idBillDetail);

        if(billDetailOptional.isEmpty()){
            throw new RuntimeException("Hóa đơn không tồn tại!");
        }

        Optional<ProductDetail> optionalProductDetail = productDetailRepository.findById(idProductDetail);

        if(optionalProductDetail.isEmpty()){
            throw new RuntimeException("Sản phẩm không tồn tại!");
        }
        Integer newQuantity = billDetailOptional.get().getQuantity() + optionalProductDetail.get().getQuantity();

        optionalProductDetail.get().setQuantity(newQuantity);

        productDetailRepository.save(optionalProductDetail.get());

        billDetailByEmployeeRepository.delete(billDetailOptional.get());
        notificationController.sendNotification();
    }
}
