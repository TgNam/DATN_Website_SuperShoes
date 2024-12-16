package org.example.datn_website_supershoes.service;


import org.example.datn_website_supershoes.Enum.Status;
import org.example.datn_website_supershoes.dto.request.BillDetailRequest;
import org.example.datn_website_supershoes.dto.request.ProductDetailPromoRequest;
import org.example.datn_website_supershoes.dto.response.BillDetailResponse;
import org.example.datn_website_supershoes.dto.response.BillDetailStatisticalProductRespone;
import org.example.datn_website_supershoes.dto.response.BillStatisticalPieResponse;
import org.example.datn_website_supershoes.dto.response.ProductPromotionResponse;
import org.example.datn_website_supershoes.model.*;
import org.example.datn_website_supershoes.repository.*;
import org.example.datn_website_supershoes.webconfig.NotificationController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BillDetailService {

    @Autowired
    BillDetailRepository billDetailRepository;

    @Autowired
    BillRepository billRepository;

    @Autowired
    ProductDetailRepository productDetailRepository;
    @Autowired
    PromotionDetailRepository promotionDetailRepository;
    @Autowired
    VoucherRepository voucherRepository;
    @Autowired
    PayBillRepository payBillRepository;
    @Autowired
    NotificationController notificationController;
    private static final BigDecimal SHIPPING_PRICE = BigDecimal.valueOf(30000);
    public Page<BillDetailResponse> getBillDetails(Specification<BillDetail> spec, Pageable pageable) {
        return billDetailRepository.findAll(spec, pageable).map(this::convertToBillDetailResponse);
    }

    // Convert BillDetail to BillDetailResponse
    private BillDetailResponse convertToBillDetailResponse(BillDetail billDetail) {
        BillDetailResponse response = new BillDetailResponse();
        response.setId(billDetail.getId());
        response.setQuantity(billDetail.getQuantity());
        response.setStatus(billDetail.getStatus());
        response.setPriceDiscount(billDetail.getBill().getPriceDiscount());
        response.setTotalMerchandise(billDetail.getBill().getTotalMerchandise());
        response.setTotalAmount(billDetail.getBill().getTotalAmount());
        response.setType(billDetail.getBill().getType());

        return response;
    }

    //dùng cho bán hàng
    @Transactional
    public void createBillDetailByIdBill(String codeBill, List<ProductDetailPromoRequest> listProductDetail) {
        // Tìm kiếm hóa đơn theo mã hóa đơn
        Optional<Bill> billOptional = billRepository.findByCodeBill(codeBill);

        // Kiểm tra hóa đơn theo mã hóa đơn có tồn tại hay không
        if (billOptional.isEmpty()) {
            throw new RuntimeException("Mã hóa đơn " + codeBill + " không tồn tại trong hệ thống.");
        }
        if (!billOptional.get().getStatus().equals(Status.WAITING_FOR_PAYMENT.toString())) {
            throw new RuntimeException("Hóa đơn " + codeBill + " không còn ở trạng thái thanh toán!");
        }
        Bill bill = billOptional.get();

        // Thực hiện thêm hóa đơn chi tiết
        for (ProductDetailPromoRequest request : listProductDetail) {
            // Tìm kiếm sản phẩm chi tiết theo id và trạng thái
            ProductDetail productDetail = productDetailRepository.findById(request.getIdProductDetail())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy tài nguyên sản phẩm trong hệ thống!"));

            // Kiểm tra sản phẩm có tồn tại không
            if (productDetail.getStatus().equals(Status.INACTIVE.toString())) {
                throw new RuntimeException("Sản phẩm " + productDetail.getProduct().getName() + " hiện tại đã ngừng bán.");
            }

            //Số lượng sản phẩm của sản phẩm chi tiết
            int quantityProductDetail = productDetail.getQuantity();

            // Kiểm tra số lượng sản phẩm số lượng mua so với sản phẩm còn lại trong kho
            if (request.getQuantity() > quantityProductDetail || quantityProductDetail <= 0) {
                throw new RuntimeException("Sản phẩm " + productDetail.getProduct().getName() + " không đủ số lượng.");
            }

            //Giá tiền sản phẩm
            BigDecimal priceProduct = productDetail.getPrice();

            //Tìm kiếm sản phẩm có đang  sale hay không
            Optional<ProductPromotionResponse> productPromotionResponse = productDetailRepository.findProductPromotionByIdProductDetail(request.getIdProductDetail());

            //Kiểm tra xem nếu sản phẩm đang sale
            if (productPromotionResponse.isPresent()) {
                //Tìm kiếm đợt giảm giá chi tiết
                Optional<PromotionDetail> promotionDetail = promotionDetailRepository.findByIdAndAndStatus(productPromotionResponse.get().getIdPromotionDetail(), Status.ONGOING.toString());
                //Số lượng sản phẩm sale
                int quantityProductPromotion = productPromotionResponse.get().getQuantityPromotionDetail();
                //Giá sản phẩm sau khi sale
                BigDecimal promotionPrice = productPromotionResponse.get().getProductDetailPrice()
                        .multiply(BigDecimal.valueOf(1 - productPromotionResponse.get().getValue() / 100))
                        .setScale(0, RoundingMode.DOWN); // Làm tròn đến 2 chữ số thập phân
                //Nếu số lượng sản phẩm khách hàng mua nhỏ hơn số lượng đang sale
                if (request.getQuantity() <= quantityProductPromotion) {
                    // Tìm hóa đơn chi tiết theo id hóa đơn và id sản phẩm chi tiết, giá sản phẩm
                    Optional<BillDetail> billDetailOptional = billDetailRepository.findByIdBillAndIdProductDetailAndPriceDiscount(bill.getId(), request.getIdProductDetail(), promotionPrice);
                    //Kiểm tra xem trong bill đã tồn tại sản phẩm đó chưa
                    if (billDetailOptional.isPresent()) {
                        //Đã tồn tại sẽ công thêm số lượng
                        BillDetail existingBillDetail = billDetailOptional.get();
                        existingBillDetail.setQuantity(existingBillDetail.getQuantity() + request.getQuantity());
                        billDetailRepository.save(existingBillDetail);
                    } else {
                        //Chưa tồn tại sẽ tạo hóa đơn chi tiết mới
                        // Tạo hóa đơn chi tiết mới
                        BillDetail billDetail = new BillDetail();
                        billDetail.setProductDetail(productDetail);
                        billDetail.setBill(bill);
                        billDetail.setQuantity(request.getQuantity());
                        billDetail.setStatus(Status.WAITING_FOR_PAYMENT.toString());
                        //Áp dụng giá sale
                        billDetail.setPriceDiscount(promotionPrice);
                        billDetailRepository.save(billDetail);
                    }
                    //Số lượng còn lại của sản phẩm sale
                    Integer newPromotionQuantity = quantityProductPromotion - request.getQuantity();

                    //Cập nhật số lượng cho sản phẩm sale
                    promotionDetail.get().setQuantity(newPromotionQuantity);

                    //Cập nhận lại số lượng giảm giá
                    if (newPromotionQuantity <= 0) {
                        promotionDetail.get().setStatus(Status.FINISHED.toString());
                    }
                    //Cập nhật lại sản phản phẩm sale
                    promotionDetailRepository.save(promotionDetail.get());
                } else {

                    // Số lượng bán lẻ khách hàng sẽ mua ngoài số lượng sale
                    int retailQuantity = request.getQuantity() - quantityProductPromotion;

                    // Tìm hóa đơn chi tiết cho sản phẩm với giá sale
                    Optional<BillDetail> billDetailWithDiscount = billDetailRepository
                            .findByIdBillAndIdProductDetailAndPriceDiscount(bill.getId(), request.getIdProductDetail(), promotionPrice);

                    // Tìm hóa đơn chi tiết cho sản phẩm với giá gốc
                    Optional<BillDetail> billDetailWithOriginalPrice = billDetailRepository
                            .findByIdBillAndIdProductDetailAndPriceDiscount(bill.getId(), request.getIdProductDetail(), priceProduct);

                    // Xử lý cho sản phẩm sale
                    if (billDetailWithDiscount.isPresent()) {
                        // Nếu hóa đơn chi tiết với giá sale đã tồn tại, cập nhật số lượng
                        BillDetail existingBillDetail = billDetailWithDiscount.get();
                        existingBillDetail.setQuantity(existingBillDetail.getQuantity() + quantityProductPromotion);
                        billDetailRepository.save(existingBillDetail);
                    } else {
                        // Nếu chưa tồn tại, tạo hóa đơn chi tiết mới với giá sale
                        BillDetail billDetail = new BillDetail();
                        billDetail.setProductDetail(productDetail);
                        billDetail.setBill(bill);
                        billDetail.setQuantity(quantityProductPromotion);
                        billDetail.setStatus(Status.WAITING_FOR_PAYMENT.toString());
                        billDetail.setPriceDiscount(promotionPrice);
                        billDetailRepository.save(billDetail);
                    }

                    // Xử lý cho sản phẩm bán lẻ với giá gốc
                    if (retailQuantity > 0) {
                        if (billDetailWithOriginalPrice.isPresent()) {
                            // Nếu hóa đơn chi tiết với giá gốc đã tồn tại, cập nhật số lượng
                            BillDetail existingBillDetail = billDetailWithOriginalPrice.get();
                            existingBillDetail.setQuantity(existingBillDetail.getQuantity() + retailQuantity);
                            billDetailRepository.save(existingBillDetail);
                        } else {
                            // Nếu chưa tồn tại, tạo hóa đơn chi tiết mới với giá gốc
                            BillDetail billDetail = new BillDetail();
                            billDetail.setProductDetail(productDetail);
                            billDetail.setBill(bill);
                            billDetail.setQuantity(retailQuantity);
                            billDetail.setStatus(Status.WAITING_FOR_PAYMENT.toString());
                            billDetail.setPriceDiscount(priceProduct);
                            billDetailRepository.save(billDetail);
                        }
                    }

                    // Cập nhật lại thông tin sản phẩm sale trong PromotionDetail
                    promotionDetail.get().setQuantity(0);
                    promotionDetail.get().setStatus(Status.FINISHED.toString());
                    promotionDetailRepository.save(promotionDetail.get());

                }
            }
            //Trường hợp sản phẩm không sale
            else {
                // Tìm hóa đơn chi tiết theo id hóa đơn và id sản phẩm chi tiết, giá sản phẩm
                Optional<BillDetail> billDetailOptional = billDetailRepository.findByIdBillAndIdProductDetailAndPriceDiscount(bill.getId(), request.getIdProductDetail(), priceProduct);
                //Kiểm tra xem sản phẩm đã có trong hóa đơn chưa
                if (billDetailOptional.isPresent()) {
                    //Nếu có sẽ cộng số lượng mua
                    BillDetail existingBillDetail = billDetailOptional.get();
                    existingBillDetail.setQuantity(existingBillDetail.getQuantity() + request.getQuantity());
                    billDetailRepository.save(existingBillDetail);
                } else {
                    //Nếu không tồn tại sẽ tạo hóa đơn mới
                    // Tạo hóa đơn chi tiết mới
                    BillDetail billDetail = new BillDetail();
                    billDetail.setProductDetail(productDetail);
                    billDetail.setBill(bill);
                    billDetail.setQuantity(request.getQuantity());
                    billDetail.setStatus(Status.WAITING_FOR_PAYMENT.toString());
                    //Áp dụng giá gốc của sản phẩm
                    billDetail.setPriceDiscount(productDetail.getPrice());
                    billDetailRepository.save(billDetail);
                }
            }
            //Số lượng còn lại của sản phẩm
            Integer newProductQuantity = quantityProductDetail - request.getQuantity();
            //Cập nhật số lượng cho sản phẩm
            productDetail.setQuantity(newProductQuantity);
            //Nếu số lượng <= 0 thì chuyển productDetail sang trạng thái INACTIVE
            if (newProductQuantity <= 0) {
                productDetail.setStatus(Status.INACTIVE.toString());
            }
            //Cập nhật lại sản phẩm chi tiết
            productDetailRepository.save(productDetail);
            notificationController.sendNotification();
        }
    }

    @Transactional
    public void updateBillAndCreateBillDetailByIdBill(String codeBill, List<ProductDetailPromoRequest> listProductDetail) {
        // Tìm kiếm hóa đơn theo mã hóa đơn
        Optional<Bill> billOptional = billRepository.findByCodeBill(codeBill);

        // Kiểm tra hóa đơn theo mã hóa đơn có tồn tại hay không
        if (!billOptional.isPresent()) {
            throw new RuntimeException("Mã hóa đơn " + codeBill + " không tồn tại trong hệ thống.");
        }

        if (!billOptional.get().getStatus().equals(Status.PENDING.toString())) {
            throw new RuntimeException("Hóa đơn " + codeBill + " không còn ở trạng thái chờ xác nhận.");
        }
        Bill bill = billOptional.get();

        // Thực hiện thêm hóa đơn chi tiết
        for (ProductDetailPromoRequest request : listProductDetail) {
            // Tìm kiếm sản phẩm chi tiết theo id và trạng thái
            Optional<ProductDetail> productDetailOptional = productDetailRepository.findByIdAndAndStatus(request.getIdProductDetail(), Status.ACTIVE.toString());

            // Kiểm tra sản phẩm có tồn tại không
            if (!productDetailOptional.isPresent()) {
                throw new RuntimeException("Id " + request.getIdProductDetail() + " của sản phẩm không tồn tại trong hệ thống.");
            }

            //Số lượng sản phẩm của sản phẩm chi tiết
            int quantityProductDetail = productDetailOptional.get().getQuantity();

            // Kiểm tra số lượng sản phẩm số lượng mua so với sản phẩm còn lại trong kho
            if (request.getQuantity() > quantityProductDetail || quantityProductDetail <= 0) {
                throw new RuntimeException("Sản phẩm " + productDetailOptional.get().getProduct().getName() + " không đủ số lượng.");
            }

            Integer sumQuantity = billDetailRepository.sumQuantityBillDetailByIdBillAdnIdProductDetail(bill.getId(), productDetailOptional.get().getId());
            if (sumQuantity == null) {
                sumQuantity = 0; // Đặt giá trị mặc định là 0 nếu sumQuantity là null
            }
            if (sumQuantity >= quantityProductDetail) {
                throw new RuntimeException("Hiện tại sản phẩm " + productDetailOptional.get().getProduct().getName() + " đã có " + sumQuantity + " sản phẩm trong giỏ hàng");
            }

            //Giá tiền sản phẩm
            BigDecimal priceProduct = productDetailOptional.get().getPrice();

            //Tìm kiếm sản phẩm có đang  sale hay không
            Optional<ProductPromotionResponse> productPromotionResponse = productDetailRepository.findProductPromotionByIdProductDetail(request.getIdProductDetail());

            //Kiểm tra xem nếu sản phẩm đang sale
            if (productPromotionResponse.isPresent()) {
                //Tìm kiếm đợt giảm giá chi tiết
                Optional<PromotionDetail> promotionDetail = promotionDetailRepository.findByIdAndAndStatus(productPromotionResponse.get().getIdPromotionDetail(), Status.ONGOING.toString());
                if (!promotionDetail.isPresent()) {
                    throw new RuntimeException("Promotion detail not found or not ongoing.");
                }
                //Số lượng sản phẩm sale
                int quantityProductPromotion = productPromotionResponse.get().getQuantityPromotionDetail();
                //Giá sản phẩm sau khi sale
                BigDecimal promotionPrice = productPromotionResponse.get().getProductDetailPrice()
                        .multiply(BigDecimal.valueOf(1 - productPromotionResponse.get().getValue() / 100))
                        .setScale(0, RoundingMode.DOWN); // Làm tròn đến 2 chữ số thập phân
                //Nếu số lượng sản phẩm khách hàng mua nhỏ hơn số lượng đang sale
                if (request.getQuantity() <= quantityProductPromotion) {

                    // Tìm hóa đơn chi tiết theo id hóa đơn và id sản phẩm chi tiết, giá sản phẩm
                    Optional<BillDetail> billDetailOptional = billDetailRepository.findByIdBillAndIdProductDetailAndPriceDiscount(bill.getId(), request.getIdProductDetail(), promotionPrice);
                    //Kiểm tra xem trong bill đã tồn tại sản phẩm đó chưa
                    if (billDetailOptional.isPresent()) {
                        //Đã tồn tại sẽ công thêm số lượng
                        BillDetail existingBillDetail = billDetailOptional.get();
                        int newQuantityBill = existingBillDetail.getQuantity() + request.getQuantity();
                        if (newQuantityBill > quantityProductDetail) {
                            throw new RuntimeException("Hiện tại đã có " + existingBillDetail.getQuantity() + " trong hóa đơn!");
                        }
                        existingBillDetail.setQuantity(newQuantityBill);
                        billDetailRepository.save(existingBillDetail);

                    } else {
                        //Chưa tồn tại sẽ tạo hóa đơn chi tiết mới
                        // Tạo hóa đơn chi tiết mới
                        BillDetail billDetail = new BillDetail();
                        billDetail.setProductDetail(productDetailOptional.get());
                        billDetail.setBill(bill);
                        billDetail.setQuantity(request.getQuantity());
                        billDetail.setStatus(Status.WAITING_FOR_PAYMENT.toString());
                        //Áp dụng giá sale
                        billDetail.setPriceDiscount(promotionPrice);
                        billDetailRepository.save(billDetail);
                    }
                    //Số lượng còn lại của sản phẩm sale
                    Integer newPromotionQuantity = quantityProductPromotion - request.getQuantity();

                    //Cập nhật số lượng cho sản phẩm sale
                    promotionDetail.get().setQuantity(newPromotionQuantity);

                    //Cập nhận lại số lượng giảm giá
                    if (newPromotionQuantity <= 0) {
                        promotionDetail.get().setStatus(Status.FINISHED.toString());
                    }
                    //Cập nhật lại sản phản phẩm sale
                    promotionDetailRepository.save(promotionDetail.get());
                    //cập nhật lại tổng giá sản phẩm
                    bill.setTotalMerchandise(
                            bill.getTotalMerchandise().add(
                                    promotionPrice.multiply(BigDecimal.valueOf(request.getQuantity()))
                            )
                    );
                } else {
                    // Số lượng bán lẻ khách hàng sẽ mua ngoài số lượng sale
                    int retailQuantity = request.getQuantity() - quantityProductPromotion;

                    // Tìm hóa đơn chi tiết cho sản phẩm với giá sale
                    Optional<BillDetail> billDetailWithDiscount = billDetailRepository
                            .findByIdBillAndIdProductDetailAndPriceDiscount(bill.getId(), request.getIdProductDetail(), promotionPrice);

                    // Tìm hóa đơn chi tiết cho sản phẩm với giá gốc
                    Optional<BillDetail> billDetailWithOriginalPrice = billDetailRepository
                            .findByIdBillAndIdProductDetailAndPriceDiscount(bill.getId(), request.getIdProductDetail(), priceProduct);

                    int discountQuantity = billDetailWithDiscount.isPresent() ? billDetailWithDiscount.get().getQuantity() : 0;
                    int originalQuantity = billDetailWithOriginalPrice.isPresent() ? billDetailWithOriginalPrice.get().getQuantity() : 0;

                    if ((discountQuantity + originalQuantity + request.getQuantity()) > quantityProductDetail) {
                        throw new RuntimeException("Hiện tại đã có " + (discountQuantity + originalQuantity) + " trong hóa đơn!");
                    }

                    // Xử lý cho sản phẩm sale
                    if (billDetailWithDiscount.isPresent()) {
                        // Nếu hóa đơn chi tiết với giá sale đã tồn tại, cập nhật số lượng
                        BillDetail existingBillDetail = billDetailWithDiscount.get();
                        existingBillDetail.setQuantity(existingBillDetail.getQuantity() + quantityProductPromotion);
                        billDetailRepository.save(existingBillDetail);
                    } else {
                        // Nếu chưa tồn tại, tạo hóa đơn chi tiết mới với giá sale
                        BillDetail billDetail = new BillDetail();
                        billDetail.setProductDetail(productDetailOptional.get());
                        billDetail.setBill(bill);
                        billDetail.setQuantity(quantityProductPromotion);
                        billDetail.setStatus(Status.WAITING_FOR_PAYMENT.toString());
                        billDetail.setPriceDiscount(promotionPrice);
                        billDetailRepository.save(billDetail);
                    }
                    bill.setTotalMerchandise(
                            bill.getTotalMerchandise().add(
                                    promotionPrice.multiply(BigDecimal.valueOf(quantityProductPromotion))
                            )
                    );
                    // Xử lý cho sản phẩm bán lẻ với giá gốc
                    if (retailQuantity > 0) {
                        if (billDetailWithOriginalPrice.isPresent()) {
                            // Nếu hóa đơn chi tiết với giá gốc đã tồn tại, cập nhật số lượng
                            BillDetail existingBillDetail = billDetailWithOriginalPrice.get();
                            existingBillDetail.setQuantity(existingBillDetail.getQuantity() + retailQuantity);
                            billDetailRepository.save(existingBillDetail);
                        } else {
                            // Nếu chưa tồn tại, tạo hóa đơn chi tiết mới với giá gốc
                            BillDetail billDetail = new BillDetail();
                            billDetail.setProductDetail(productDetailOptional.get());
                            billDetail.setBill(bill);
                            billDetail.setQuantity(retailQuantity);
                            billDetail.setStatus(Status.WAITING_FOR_PAYMENT.toString());
                            billDetail.setPriceDiscount(priceProduct);
                            billDetailRepository.save(billDetail);
                        }
                        bill.setTotalMerchandise(
                                bill.getTotalMerchandise().add(
                                        priceProduct.multiply(BigDecimal.valueOf(retailQuantity))
                                )
                        );
                    }

                    // Cập nhật lại thông tin sản phẩm sale trong PromotionDetail
                    promotionDetail.get().setQuantity(0);
                    promotionDetail.get().setStatus(Status.FINISHED.toString());
                    promotionDetailRepository.save(promotionDetail.get());

                }
            }
            //Trường hợp sản phẩm không sale
            else {
                // Tìm hóa đơn chi tiết theo id hóa đơn và id sản phẩm chi tiết, giá sản phẩm
                Optional<BillDetail> billDetailOptional = billDetailRepository.findByIdBillAndIdProductDetailAndPriceDiscount(bill.getId(), request.getIdProductDetail(), priceProduct);
                //Kiểm tra xem sản phẩm đã có trong hóa đơn chưa
                if (billDetailOptional.isPresent()) {
                    //Nếu có sẽ cộng số lượng mua
                    BillDetail existingBillDetail = billDetailOptional.get();
                    int newQuantityBill = existingBillDetail.getQuantity() + request.getQuantity();
                    if (newQuantityBill > quantityProductDetail) {
                        throw new RuntimeException("Hiện tại đã có " + existingBillDetail.getQuantity() + " trong hóa đơn!");
                    }
                    existingBillDetail.setQuantity(newQuantityBill);
                    billDetailRepository.save(existingBillDetail);
                } else {
                    //Nếu không tồn tại sẽ tạo hóa đơn mới
                    // Tạo hóa đơn chi tiết mới
                    BillDetail billDetail = new BillDetail();
                    billDetail.setProductDetail(productDetailOptional.get());
                    billDetail.setBill(bill);
                    billDetail.setQuantity(request.getQuantity());
                    billDetail.setStatus(Status.WAITING_FOR_PAYMENT.toString());
                    //Áp dụng giá gốc của sản phẩm
                    billDetail.setPriceDiscount(productDetailOptional.get().getPrice());
                    billDetailRepository.save(billDetail);
                }
                bill.setTotalMerchandise(
                        bill.getTotalMerchandise().add(
                                productDetailOptional.get().getPrice().multiply(BigDecimal.valueOf(request.getQuantity()))
                        )
                );
            }
        }

        BigDecimal priceDiscount = BigDecimal.ZERO;
        BigDecimal totalAmount = bill.getTotalMerchandise();
        if(!billOptional.get().getAddress().isEmpty()){
            totalAmount = totalAmount.add(SHIPPING_PRICE);
        }
        if (bill.getVoucher() != null) {
            Optional<Voucher> voucherOptional = voucherRepository.findById(bill.getVoucher().getId());
            if (voucherOptional.isPresent()) {
                Voucher voucher = voucherOptional.get();
                if (bill.getTotalMerchandise().compareTo(voucher.getMinBillValue()) >= 0) {
                    BigDecimal priceSale = bill.getTotalMerchandise().multiply(BigDecimal.valueOf(voucher.getValue() / 100.0))
                            .setScale(0, RoundingMode.DOWN);
                    BigDecimal maximumDiscount = voucher.getMaximumDiscount().max(BigDecimal.ZERO);
                    priceDiscount = priceSale.compareTo(maximumDiscount) <= 0 ? priceSale : maximumDiscount;
                    totalAmount = bill.getTotalMerchandise().subtract(priceDiscount);
                }
            }
        }
        bill.setPriceDiscount(priceDiscount);
        bill.setTotalAmount(totalAmount);
        Bill updateBill = billRepository.save(bill);
        Optional<PayBill> optionalPayBill = payBillRepository.findByBill(updateBill);
        if (optionalPayBill.isPresent()) {
            optionalPayBill.get().setAmount(totalAmount);
        }
        payBillRepository.save(optionalPayBill.get());
        notificationController.sendNotification();
    }

    public List<BillDetailStatisticalProductRespone> getBillStatistics() {
        // Fetch raw data from the repository
        List<Object[]> rawData = billDetailRepository.findProductDetailsForBillDetails();

        // Map raw data to BillDetailStatisticalProductRespone objects
        return rawData.stream()
                .map(data -> new BillDetailStatisticalProductRespone(
                        (byte[]) data[0],
                        (Long) data[1],  // Assuming first element is image (byte[])
                        (String) data[2],       // Assuming second element is product name
                        (Integer) data[3],      // Assuming third element is quantity
                        (BigDecimal) data[4],
                        (BigDecimal) data[5]   // Assuming fourth element is priceDiscount
                ))
                .collect(Collectors.toList());
    }
    @Transactional
    public BillDetail plusBillDetail(String codeBill, Long idBillDetail, Long idProductDetail) {
        Bill bill = validateAndGetBill(codeBill, Status.PENDING);
        BillDetail billDetail = billDetailRepository.findById(idBillDetail)
                .orElseThrow(() -> new RuntimeException("Hóa đơn không tồn tại!"));

        ProductPromotionResponse productPromotionResponse = productDetailRepository.findProductDetailByIdProductDetail(idProductDetail)
                .orElseThrow(() -> new RuntimeException("Sản phẩm với ID " + idProductDetail + " không tồn tại!"));

        BigDecimal productPrice = calculateDiscountedPrice(productPromotionResponse);
        validateProductAvailability(productPromotionResponse, billDetailRepository.sumQuantityBillDetailByIdBillAdnIdProductDetail(bill.getId(), idProductDetail));
        if((billDetail.getPriceDiscount().setScale(0, RoundingMode.DOWN).compareTo(productPrice.setScale(0, RoundingMode.DOWN)) != 0)){
            throw new RuntimeException("Sản phẩm với mức giá " + billDetail.getPriceDiscount() + " VND đã hết hàng!");
        }
        billDetail.setQuantity(billDetail.getQuantity() + 1);
        bill.setTotalMerchandise(bill.getTotalMerchandise().add(productPrice));

        if (productPromotionResponse.getValue() != null && productPromotionResponse.getValue() > 0) {
            PromotionDetail promotionDetail = promotionDetailRepository.findByIdAndAndStatus(
                            productPromotionResponse.getIdPromotionDetail(), Status.ONGOING.toString())
                    .orElseThrow(() -> new RuntimeException("Xảy ra lỗi khi xử lý sản phẩm giảm giá!"));
            updatePromotionDetail(promotionDetail, 1);
        }

        BigDecimal totalAmount = applyVoucher(bill, bill.getTotalMerchandise());
        bill.setTotalAmount(totalAmount);

        billRepository.save(bill);
        // Cập nhật PayBill
        updatePayBill(bill);

        notificationController.sendNotification();
        return billDetailRepository.save(billDetail);
    }

    @Transactional
    public BillDetail subtractBillDetail(String codeBill, Long idBillDetail, Long idProductDetail) {
        Bill bill = validateAndGetBill(codeBill, Status.PENDING);
        BillDetail billDetail = billDetailRepository.findById(idBillDetail)
                .orElseThrow(() -> new RuntimeException("Hóa đơn không tồn tại!"));

        // Cập nhật số lượng
        int newQuantity = billDetail.getQuantity() - 1;
        if (newQuantity <= 0) {
            throw new RuntimeException("Tối thiểu phải có 1 sản phẩm");
        }
        billDetail.setQuantity(newQuantity);

        // Cập nhật tổng tiền hàng
        BigDecimal priceProductDetail = billDetail.getPriceDiscount();
        bill.setTotalMerchandise(bill.getTotalMerchandise().subtract(priceProductDetail));

        // Áp dụng voucher (nếu có)
        bill.setTotalAmount(applyVoucher(bill, bill.getTotalMerchandise()));

        // Cập nhật PayBill
        updatePayBill(bill);

        // Lưu lại các thay đổi
        billRepository.save(bill);
        BillDetail updatedBillDetail = billDetailRepository.save(billDetail);

        // Gửi thông báo
        notificationController.sendNotification();
        return updatedBillDetail;
    }

    @Transactional
    public void deleteBillDetail(String codeBill, Long idBillDetail, Long idProductDetail) {
        Bill bill = validateAndGetBill(codeBill, Status.PENDING);
        List<BillDetail> billDetails = billDetailRepository.findByIdBill(bill.getId());

        if (billDetails.size() <= 1) {
            throw new RuntimeException("Cần tối thiểu 1 sản phẩm có trong hóa đơn");
        }

        BillDetail billDetail = billDetailRepository.findById(idBillDetail)
                .orElseThrow(() -> new RuntimeException("Hóa đơn không tồn tại!"));

        // Trừ tổng tiền hàng
        BigDecimal totalToSubtract = billDetail.getPriceDiscount()
                .multiply(BigDecimal.valueOf(billDetail.getQuantity()));
        bill.setTotalMerchandise(bill.getTotalMerchandise().subtract(totalToSubtract));

        // Áp dụng voucher (nếu có)
        bill.setTotalAmount(applyVoucher(bill, bill.getTotalMerchandise()));

        // Cập nhật PayBill
        updatePayBill(bill);

        // Lưu thay đổi và xóa BillDetail
        billRepository.save(bill);
        billDetailRepository.delete(billDetail);

        // Gửi thông báo
        notificationController.sendNotification();
    }
    private BigDecimal calculateDiscountedPrice(ProductPromotionResponse productPromotionResponse) {
        BigDecimal price = productPromotionResponse.getProductDetailPrice();
        if (productPromotionResponse.getValue() != null && productPromotionResponse.getValue() > 0) {
            price = price.multiply(BigDecimal.valueOf(1 - productPromotionResponse.getValue() / 100))
                    .setScale(0, RoundingMode.DOWN);
        }
        return price;
    }

    private Bill validateAndGetBill(String codeBill, Status expectedStatus) {
        Bill bill = billRepository.findByCodeBill(codeBill)
                .orElseThrow(() -> new RuntimeException("Mã hóa đơn " + codeBill + " không tồn tại trong hệ thống."));
        if (!bill.getStatus().equals(expectedStatus.toString())) {
            throw new RuntimeException("Hóa đơn " + codeBill + " không còn ở trạng thái " + expectedStatus + "!");
        }
        return bill;
    }

    private void updatePayBill(Bill bill) {
        PayBill payBill = payBillRepository.findByBill(bill)
                .orElseThrow(() -> new RuntimeException("Xảy ra lỗi thanh toán hóa đơn!"));
        payBill.setAmount(bill.getTotalAmount());
        payBillRepository.save(payBill);
    }
    private void validateProductAvailability(ProductPromotionResponse productPromotionResponse, Integer sumQuantity) {
        if (productPromotionResponse.getQuantityProductDetail() <= 0) {
            throw new RuntimeException("Sản phẩm " + productPromotionResponse.getNameProduct() + " đã hết hàng");
        }
        if (sumQuantity >= productPromotionResponse.getQuantityProductDetail()) {
            throw new RuntimeException("Hiện tại sản phẩm " + productPromotionResponse.getNameProduct() + " đã có " + sumQuantity + " sản phẩm trong giỏ hàng");
        }
    }
    private void updatePromotionDetail(PromotionDetail promotionDetail, int quantityToReduce) {
        int newQuantity = promotionDetail.getQuantity() - quantityToReduce;
        promotionDetail.setQuantity(newQuantity);
        if (newQuantity <= 0) {
            promotionDetail.setStatus(Status.FINISHED.toString());
        }
        promotionDetailRepository.save(promotionDetail);
    }
    private BigDecimal applyVoucher(Bill bill, BigDecimal totalMerchandise) {
        if(!bill.getAddress().isEmpty()){
            totalMerchandise = totalMerchandise.add(SHIPPING_PRICE);
        }
        if (bill.getVoucher() == null) return totalMerchandise;

        Voucher voucher = voucherRepository.findById(bill.getVoucher().getId())
                .orElse(null);
        if (voucher == null || totalMerchandise.compareTo(voucher.getMinBillValue()) < 0) {
            return totalMerchandise;
        }
        BigDecimal discount = totalMerchandise.multiply(BigDecimal.valueOf(voucher.getValue() / 100.0))
                .setScale(0, RoundingMode.DOWN);
        discount = discount.min(voucher.getMaximumDiscount());
        return totalMerchandise.subtract(discount);
    }


}
