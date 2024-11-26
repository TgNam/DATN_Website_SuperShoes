package org.example.datn_website_supershoes.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.datn_website_supershoes.Enum.Status;
import org.example.datn_website_supershoes.dto.request.ProductDetailRequest;
import org.example.datn_website_supershoes.dto.request.updateProduct.UpdateProductDetailRequest;
import org.example.datn_website_supershoes.dto.response.*;
import org.example.datn_website_supershoes.model.*;
import org.example.datn_website_supershoes.repository.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ProductDetailService {
    @Autowired
    private ProductDetailRepository productDetailRepository;
    @Autowired
    private SizeRepository sizeRepository;
    @Autowired
    private ColorRepository colorRepository;
    @Autowired
    private ProductImageService productImageService;
    @Autowired
    private ProductImageRepository productImageRepository;

    @Transactional
    public boolean createProductDetail(Product product, List<ProductDetailRequest> productDetailRequest) {
        try {
            for (ProductDetailRequest request : productDetailRequest) {
                Optional<Size> optionalSize = sizeRepository.findByIdAndStatus(request.getIdSize(), Status.ACTIVE.toString());
                Optional<Color> optionalColor = colorRepository.findByIdAndStatus(request.getIdColor(), Status.ACTIVE.toString());
                if (optionalSize.isEmpty()) {
                    throw new RuntimeException("Id kích cỡ: " + request.getIdSize() + " không tồn tại trong hệ thống");
                }
                if (optionalColor.isEmpty()) {
                    throw new RuntimeException("Id màu sắc: " + request.getIdColor() + " không tồn tại trong hệ thống");
                }
                ProductDetail productDetail = ProductDetail.builder()
                        .quantity(request.getQuantity())
                        .price(request.getPrice())
                        .product(product)
                        .color(optionalColor.get())
                        .size(optionalSize.get())
                        .build();
                productDetail.setStatus(Status.ACTIVE.toString());
                ProductDetail saveProductDetail = productDetailRepository.save(productDetail);
                boolean checkProductDetail = productImageService.createProductImage(saveProductDetail, request.getListImage());
                if (!checkProductDetail) {
                    throw new RuntimeException("Xảy ra lỗi khi thêm ảnh cho sản phẩm chi tiết");
                }
            }
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
    public void updateProduct(List<UpdateProductDetailRequest> productDetailRequests){
        for (UpdateProductDetailRequest request : productDetailRequests) {
            Optional<ProductDetail> optionalProductDetail = productDetailRepository.findById(request.getId());
            Optional<Size> optionalSize = sizeRepository.findByIdAndStatus(request.getIdSize(), Status.ACTIVE.toString());
            Optional<Color> optionalColor = colorRepository.findByIdAndStatus(request.getIdColor(), Status.ACTIVE.toString());
            if (optionalProductDetail.isEmpty()) {
                throw new RuntimeException("Id sản phẩm chi tiết: " + request.getId() + " không tồn tại trong hệ thống");
            }
            if (optionalSize.isEmpty()) {
                throw new RuntimeException("Id kích cỡ: " + request.getIdSize() + " không tồn tại trong hệ thống");
            }
            if (optionalColor.isEmpty()) {
                throw new RuntimeException("Id màu sắc: " + request.getIdColor() + " không tồn tại trong hệ thống");
            }
            if (request.getQuantity()<=0){
                throw new RuntimeException("Vui lòng cập nhật số lượng lớn hơn 0");
            }
            ProductDetail detail = optionalProductDetail.get();
            detail.setStatus(Status.ACTIVE.toString());
            detail.setQuantity(request.getQuantity());
            detail.setPrice(request.getPrice());
            detail.setColor(optionalColor.get());
            detail.setSize(optionalSize.get());
            ProductDetail updateProductDetail = productDetailRepository.save(detail);
            if (!request.getListImage().isEmpty()){
                productImageRepository.deleteByProductDetail(updateProductDetail);
                boolean checkProductDetail = productImageService.createProductImage(updateProductDetail, request.getListImage());
                if (!checkProductDetail) {
                    throw new RuntimeException("Xảy ra lỗi khi thêm ảnh cho sản phẩm chi tiết");
                }
            }
        }
    }
    @Transactional
    public ProductDetail updateStatus(Long id, boolean aBoolean) {
        Optional<ProductDetail> optionalProductDetail = productDetailRepository.findById(id);
        if (!optionalProductDetail.isPresent()) {
            throw new RuntimeException("Id: " + id + " của sản phẩm không tồn tại");
        }
        if (optionalProductDetail.get().getQuantity() <= 0) {
            throw new RuntimeException("Vui lòng cập nhập số lượng của sản phẩm trước khi thay đổi trạng thái");
        }
        String newStatus = aBoolean ? Status.ACTIVE.toString() : Status.INACTIVE.toString();
        optionalProductDetail.get().setStatus(newStatus);
        return productDetailRepository.save(optionalProductDetail.get());
    }

    public ProductDetail getById(Long id) {
        return productDetailRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product Details not found!"));
    }

    public List<ProductDetailResponseByNam> findProductDetailRequests(List<Long> idProducts) {
        return productDetailRepository.findProductDetailRequests(idProducts);
    }

    public List<ProductPromotionResponse> findProductPromotion() {
        return productDetailRepository.findProductPromotion();
    }

    public List<ProductDetailResponseByNam> filterListProductDetail(List<Long> idProducts, String search, String nameSize, String nameColor, String priceRange) {
        return productDetailRepository.findProductDetailRequests(idProducts).stream()
                .filter(productDetailResponse -> productDetailResponse.getNameProduct().toLowerCase().contains(search.trim().toLowerCase()))
                .filter(productDetailResponse -> productDetailResponse.getNameSize().toLowerCase().contains(nameSize.trim().toLowerCase()))
                .filter(productDetailResponse -> productDetailResponse.getNameColor().toLowerCase().contains(nameColor.trim().toLowerCase()))
                .filter(productDetailResponse -> filterByPriceRange(productDetailResponse.getPrice(), priceRange))
                .collect(Collectors.toList());
    }

    public List<ProductPromotionResponse> filterListProductPromotion(String search, String nameSize, String nameColor, String priceRange) {
        return productDetailRepository.findProductPromotion().stream()
                .filter(ProductPromotionResponse -> ProductPromotionResponse.getNameProduct().toLowerCase().contains(search.trim().toLowerCase()))
                .filter(ProductPromotionResponse -> ProductPromotionResponse.getNameSize().toLowerCase().contains(nameSize.trim().toLowerCase()))
                .filter(ProductPromotionResponse -> ProductPromotionResponse.getNameColor().toLowerCase().contains(nameColor.trim().toLowerCase()))
                .filter(ProductPromotionResponse -> {
                    BigDecimal priceToFilter = ProductPromotionResponse.getProductDetailPrice();
                    return filterByPriceRange(priceToFilter, priceRange);
                })
                .collect(Collectors.toList());
    }

    public boolean filterByPriceRange(BigDecimal price, String priceRange) {
        switch (priceRange) {
            case "under500":
                // Dưới 500.000
                return price.compareTo(BigDecimal.valueOf(500000)) < 0;
            case "500to2000":
                // Từ 500.000 đến 2.000.000
                return price.compareTo(BigDecimal.valueOf(500000)) >= 0 && price.compareTo(BigDecimal.valueOf(2000000)) <= 0;
            case "above2000":
                // Từ 2.000.000 trở lên
                return price.compareTo(BigDecimal.valueOf(2000000)) > 0;
            default:
                // Nếu không có điều kiện lọc, trả về true để không lọc
                return true;
        }
    }

    public ProductDetail findProductDetailByIdProductDetail(Long idProductDetail) {
        return productDetailRepository.findById(idProductDetail).get();
    }

    public List<ProductViewCustomerReponse> getProductPriceRangeWithPromotion() {
        List<ProductViewCustomerReponse> productViewCustomerReponses = productDetailRepository.findProductPriceRangeWithPromotion();
        return productViewCustomerReponses;
    }

    public List<ProductViewCustomerReponseByQuang> getFilteredProducts(
            String nameProduct, Long idColor, Long idSize, Long idBrand, Long idCategory, BigDecimal minPrice, BigDecimal maxPrice) {
        return productDetailRepository.findProductPriceRangeWithPromotionByQuang(
                nameProduct, idColor, idSize, idBrand, idCategory, minPrice, maxPrice);
    }

    public ProductPromotionResponse findProductPromotionByIdProcuctAndIdColorAndIdSize(Long idProduct, Long idColor, Long idSize) {
        Optional<ProductPromotionResponse> productPromotionResponse = productDetailRepository.findProductPromotionByIdProcuctAndIdColorAndIdSize(idProduct, idColor, idSize);
        if (productPromotionResponse.isEmpty()) {
            throw new RuntimeException("Sản phẩm đã hết hàng");
        }
        return productPromotionResponse.get();
    }


}
