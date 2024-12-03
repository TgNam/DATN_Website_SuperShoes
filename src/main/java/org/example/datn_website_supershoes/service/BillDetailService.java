package org.example.datn_website_supershoes.service;

import jakarta.transaction.Transactional;
import org.example.datn_website_supershoes.Enum.Status;
import org.example.datn_website_supershoes.dto.request.BillDetailRequest;
import org.example.datn_website_supershoes.dto.request.ProductDetailPromoRequest;
import org.example.datn_website_supershoes.dto.response.BillDetailResponse;
import org.example.datn_website_supershoes.dto.response.BillDetailStatisticalProductRespone;
import org.example.datn_website_supershoes.dto.response.BillStatisticalPieResponse;
import org.example.datn_website_supershoes.dto.response.ProductPromotionResponse;
import org.example.datn_website_supershoes.model.*;
import org.example.datn_website_supershoes.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BillDetailService {

    @Autowired
    private BillDetailRepository billDetailRepository;

    @Autowired
    private BillRepository billRepository;

    @Autowired
    private ProductDetailRepository productDetailRepository;
    @Autowired
    private PromotionDetailRepository promotionDetailRepository;
    @Autowired
    private VoucherRepository voucherRepository;
    @Autowired
    private PayBillRepository payBillRepository;


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
    public void deleteBillDetailAndUpdateProduct(String productCode, String nameColor, String nameSize) {
        // Find the BillDetail by productCode and nameColor
        List<BillDetail> billDetails = billDetailRepository.findByProductCodeAndColorName(productCode, nameColor, nameSize);
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
        response.setPriceDiscount(billDetail.getBill().getPriceDiscount());
        response.setTotalMerchandise(billDetail.getBill().getTotalMerchandise());
        response.setTotalAmount(billDetail.getBill().getTotalAmount());

        return response;
    }

    //dùng cho bán hàng
    public void createBillDetailByIdBill(String codeBill, List<ProductDetailPromoRequest> listProductDetail) {
        try {
            // Tìm kiếm hóa đơn theo mã hóa đơn
            Optional<Bill> billOptional = billRepository.findByCodeBill(codeBill);

            // Kiểm tra hóa đơn theo mã hóa đơn có tồn tại hay không
            if (!billOptional.isPresent()) {
                throw new RuntimeException("Mã hóa đơn " + codeBill + " không tồn tại trong hệ thống.");
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

                //Giá tiền sản phẩm
                BigDecimal priceProduct = productDetailOptional.get().getPrice();

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
                            .setScale(2, RoundingMode.HALF_UP); // Làm tròn đến 2 chữ số thập phân
                    //Nếu số lượng sản phẩm khách hàng mua nhỏ hơn số lượng đang sale
                    if (request.getQuantity() <= quantityProductPromotion) {
                        System.out.println("số lượng sản phẩm khách hàng mua nhỏ hơn số lượng đang sale" + request.getQuantity() + "" + quantityProductPromotion);
                        // Tìm hóa đơn chi tiết theo id hóa đơn và id sản phẩm chi tiết, giá sản phẩm
                        Optional<BillDetail> billDetailOptional = billDetailRepository.findByIdBillAndIdProductDetailAndPriceDiscount(bill.getId(), request.getIdProductDetail(), promotionPrice);
                        System.out.println("Kiểm tra billDetailOptional ở số lượng sản phẩm khách hàng mua nhỏ hơn số lượng đang sale" + billDetailOptional.isPresent() + promotionPrice);
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
                    } else {
                        System.out.println("Số lượng sản phẩm khách hàng mua lớn hơn số lượng đang sale: " + request.getQuantity() + " > " + quantityProductPromotion);

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
                            billDetail.setProductDetail(productDetailOptional.get());
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
                                billDetail.setProductDetail(productDetailOptional.get());
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
                        billDetail.setProductDetail(productDetailOptional.get());
                        billDetail.setBill(bill);
                        billDetail.setQuantity(request.getQuantity());
                        billDetail.setStatus(Status.WAITING_FOR_PAYMENT.toString());
                        //Áp dụng giá gốc của sản phẩm
                        billDetail.setPriceDiscount(productDetailOptional.get().getPrice());
                        billDetailRepository.save(billDetail);
                    }
                }
                //Số lượng còn lại của sản phẩm
                Integer newProductQuantity = quantityProductDetail - request.getQuantity();
                //Cập nhật số lượng cho sản phẩm
                productDetailOptional.get().setQuantity(newProductQuantity);
                //Nếu số lượng <= 0 thì chuyển productDetail sang trạng thái INACTIVE
                if (newProductQuantity <= 0) {
                    productDetailOptional.get().setStatus(Status.INACTIVE.toString());
                }
                //Cập nhật lại sản phẩm chi tiết
                productDetailRepository.save(productDetailOptional.get());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Transactional
    public void updateBillAndCreateBillDetailByIdBill(String codeBill, List<ProductDetailPromoRequest> listProductDetail) {
        try {
            // Tìm kiếm hóa đơn theo mã hóa đơn
            Optional<Bill> billOptional = billRepository.findByCodeBill(codeBill);

            // Kiểm tra hóa đơn theo mã hóa đơn có tồn tại hay không
            if (!billOptional.isPresent()) {
                throw new RuntimeException("Mã hóa đơn " + codeBill + " không tồn tại trong hệ thống.");
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

                //Giá tiền sản phẩm
                BigDecimal priceProduct = productDetailOptional.get().getPrice();

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
                            .setScale(2, RoundingMode.HALF_UP); // Làm tròn đến 2 chữ số thập phân
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
                        existingBillDetail.setQuantity(existingBillDetail.getQuantity() + request.getQuantity());
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
            if (bill.getVoucher() != null) {
                Optional<Voucher> voucherOptional = voucherRepository.findById(bill.getVoucher().getId());
                if (voucherOptional.isPresent()) {
                    Voucher voucher = voucherOptional.get();
                    if (bill.getTotalMerchandise().compareTo(voucher.getMinBillValue()) >= 0) {
                        BigDecimal priceSale = bill.getTotalMerchandise().multiply(BigDecimal.valueOf(voucher.getValue() / 100.0))
                                .setScale(2, RoundingMode.HALF_UP);
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
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
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

    public BillDetail plusBillDetail(String codeBill, Long idBillDetail, Long idProductDetail) {
        // Tìm kiếm hóa đơn theo mã hóa đơn
        Optional<Bill> billOptional = billRepository.findByCodeBill(codeBill);

        // Kiểm tra hóa đơn theo mã hóa đơn có tồn tại hay không
        if (!billOptional.isPresent()) {
            throw new RuntimeException("Mã hóa đơn " + codeBill + " không tồn tại trong hệ thống.");
        }

        Bill bill = billOptional.get();

        Optional<BillDetail> billDetailOptional = billDetailRepository.findById(idBillDetail);

        if (billDetailOptional.isEmpty()) {
            throw new RuntimeException("Hóa đơn không tồn tại!");
        }

        Optional<ProductDetail> optionalProductDetail = productDetailRepository.findByIdAndAndStatusAndPrice(idProductDetail, Status.ACTIVE.toString(), billDetailOptional.get().getPriceDiscount());

        if (optionalProductDetail.isEmpty()) {
            throw new RuntimeException("Sản phẩm đã hết hàng!");
        }
        if (optionalProductDetail.get().getQuantity() <= 0) {
            throw new RuntimeException("Sản phẩm đã hết hàng");
        }
        Integer newQuantity = optionalProductDetail.get().getQuantity() - 1;
        optionalProductDetail.get().setQuantity(newQuantity);
        if (newQuantity <= 0) {
            optionalProductDetail.get().setStatus(Status.INACTIVE.toString());
        }
        productDetailRepository.save(optionalProductDetail.get());
        billDetailOptional.get().setQuantity(billDetailOptional.get().getQuantity() + 1);
        bill.setTotalMerchandise(
                bill.getTotalMerchandise().add(optionalProductDetail.get().getPrice())
        );
        BigDecimal priceDiscount = BigDecimal.ZERO;
        BigDecimal totalAmount = bill.getTotalMerchandise();
        if (bill.getVoucher() != null) {
            Optional<Voucher> voucherOptional = voucherRepository.findById(bill.getVoucher().getId());
            if (voucherOptional.isPresent()) {
                Voucher voucher = voucherOptional.get();
                if (bill.getTotalMerchandise().compareTo(voucher.getMinBillValue()) >= 0) {
                    BigDecimal priceSale = bill.getTotalMerchandise().multiply(BigDecimal.valueOf(voucher.getValue() / 100.0))
                            .setScale(2, RoundingMode.HALF_UP);
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

        return billDetailRepository.save(billDetailOptional.get());
    }

    public BillDetail subtractBillDetail(String codeBill, Long idBillDetail, Long idProductDetail) {
        // Tìm kiếm hóa đơn theo mã hóa đơn
        Optional<Bill> billOptional = billRepository.findByCodeBill(codeBill);

        // Kiểm tra hóa đơn theo mã hóa đơn có tồn tại hay không
        if (!billOptional.isPresent()) {
            throw new RuntimeException("Mã hóa đơn " + codeBill + " không tồn tại trong hệ thống.");
        }

        Bill bill = billOptional.get();

        Optional<BillDetail> billDetailOptional = billDetailRepository.findById(idBillDetail);

        if (billDetailOptional.isEmpty()) {
            throw new RuntimeException("Hóa đơn không tồn tại!");
        }

        Optional<ProductDetail> optionalProductDetail = productDetailRepository.findById(idProductDetail);

        if (optionalProductDetail.isEmpty()) {
            throw new RuntimeException("Sản phẩm không tồn tại!");
        }
        Integer newQuantity = billDetailOptional.get().getQuantity() - 1;

        if (newQuantity <= 0) {
            throw new RuntimeException("Tối thiểu phải có 1 sản phẩm");
        }
        billDetailOptional.get().setQuantity(newQuantity);

        optionalProductDetail.get().setQuantity(optionalProductDetail.get().getQuantity() + 1);

        productDetailRepository.save(optionalProductDetail.get());

        bill.setTotalMerchandise(
                bill.getTotalMerchandise().subtract(optionalProductDetail.get().getPrice())
        );

        BigDecimal priceDiscount = BigDecimal.ZERO;
        BigDecimal totalAmount = bill.getTotalMerchandise();

        if (bill.getVoucher() != null) {
            Optional<Voucher> voucherOptional = voucherRepository.findById(bill.getVoucher().getId());
            if (voucherOptional.isPresent()) {
                Voucher voucher = voucherOptional.get();
                if (bill.getTotalMerchandise().compareTo(voucher.getMinBillValue()) >= 0) {
                    BigDecimal priceSale = bill.getTotalMerchandise().multiply(BigDecimal.valueOf(voucher.getValue() / 100.0))
                            .setScale(2, RoundingMode.HALF_UP);
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

        return billDetailRepository.save(billDetailOptional.get());
    }

    public void deleteBillDetail(String codeBill, Long idBillDetail, Long idProductDetail) {
        // Tìm kiếm hóa đơn theo mã hóa đơn
        Optional<Bill> billOptional = billRepository.findByCodeBill(codeBill);

        // Kiểm tra hóa đơn theo mã hóa đơn có tồn tại hay không
        if (!billOptional.isPresent()) {
            throw new RuntimeException("Mã hóa đơn " + codeBill + " không tồn tại trong hệ thống.");
        }

        Bill bill = billOptional.get();

        List<BillDetail> billDetails = billDetailRepository.findByIdBill(bill.getId());

        if (billDetails.size() <= 1) {
            throw new RuntimeException("Cần tối thiểu 1 sản phẩm có trong hóa đơn");
        }
        Optional<BillDetail> billDetailOptional = billDetailRepository.findById(idBillDetail);

        if (billDetailOptional.isEmpty()) {
            throw new RuntimeException("Hóa đơn không tồn tại!");
        }

        Optional<ProductDetail> optionalProductDetail = productDetailRepository.findById(idProductDetail);

        if (optionalProductDetail.isEmpty()) {
            throw new RuntimeException("Sản phẩm không tồn tại!");
        }
        bill.setTotalMerchandise(
                bill.getTotalMerchandise().subtract(billDetailOptional.get().getPriceDiscount().multiply(BigDecimal.valueOf(billDetailOptional.get().getQuantity())))
        );

        BigDecimal priceDiscount = BigDecimal.ZERO;
        BigDecimal totalAmount = bill.getTotalMerchandise();

        if (bill.getVoucher() != null) {
            Optional<Voucher> voucherOptional = voucherRepository.findById(bill.getVoucher().getId());
            if (voucherOptional.isPresent()) {
                Voucher voucher = voucherOptional.get();
                if (bill.getTotalMerchandise().compareTo(voucher.getMinBillValue()) >= 0) {
                    BigDecimal priceSale = bill.getTotalMerchandise().multiply(BigDecimal.valueOf(voucher.getValue() / 100.0))
                            .setScale(2, RoundingMode.HALF_UP);
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
        billDetailRepository.delete(billDetailOptional.get());
    }

}
