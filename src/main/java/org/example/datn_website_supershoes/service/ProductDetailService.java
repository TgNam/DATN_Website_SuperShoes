package org.example.datn_website_supershoes.service;


import lombok.RequiredArgsConstructor;
import org.example.datn_website_supershoes.Enum.Status;
import org.example.datn_website_supershoes.dto.request.ProductDetailPromoRequest;
import org.example.datn_website_supershoes.dto.request.ProductDetailRequest;
import org.example.datn_website_supershoes.dto.request.updateProduct.UpdateProductDetailRequest;
import org.example.datn_website_supershoes.dto.response.*;
import org.example.datn_website_supershoes.model.*;
import org.example.datn_website_supershoes.repository.*;
import org.example.datn_website_supershoes.webconfig.NotificationController;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ProductDetailService {
    @Autowired
    ProductRepository productRepository;
    @Autowired
    ProductDetailRepository productDetailRepository;
    @Autowired
    SizeRepository sizeRepository;
    @Autowired
    ColorRepository colorRepository;
    @Autowired
    ProductImageService productImageService;
    @Autowired
    ProductImageRepository productImageRepository;
    @Autowired
    NotificationController notificationController;

    @Transactional
    public boolean createProductDetail(Product product, List<ProductDetailRequest> productDetailRequest) {
        for (ProductDetailRequest request : productDetailRequest) {
            Size size = sizeRepository.findById(request.getIdSize())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy tài nguyên kích cỡ trong hệ thống!"));
            if(size.getStatus().equals(Status.INACTIVE.toString())){
                throw new RuntimeException("Kích cỡ với " + size.getName() + " đã ngừng hoạt động!");
            }
            Color color = colorRepository.findById(request.getIdColor())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy tài nguyên màu sắc trong hệ thống!"));
            ProductDetail productDetail = ProductDetail.builder()
                    .quantity(request.getQuantity())
                    .price(request.getPrice())
                    .product(product)
                    .color(color)
                    .size(size)
                    .build();
            productDetail.setStatus(Status.ACTIVE.toString());
            ProductDetail saveProductDetail = productDetailRepository.save(productDetail);
            boolean checkProductDetail = productImageService.createProductImage(saveProductDetail, request.getListImage());
            if (!checkProductDetail) {
                throw new RuntimeException("Xảy ra lỗi khi thêm ảnh cho sản phẩm chi tiết");
            }
        }
        return true;
    }
    @Transactional
    public void updateProduct(List<UpdateProductDetailRequest> productDetailRequests) {
        for (UpdateProductDetailRequest request : productDetailRequests) {
            Optional<ProductDetail> optionalProductDetail = productDetailRepository.findById(request.getId());
            if (optionalProductDetail.isEmpty()) {
                throw new RuntimeException("Sản phẩm chi tiết với Id là " + request.getId() + " không tồn tại trong hệ thống");
            }
            if (request.getQuantity() <= 0) {
                throw new RuntimeException("Vui lòng cập nhật số lượng lớn hơn 0");
            }
            ProductDetail detail = optionalProductDetail.get();
            detail.setStatus(Status.ACTIVE.toString());
            detail.setQuantity(request.getQuantity());
            detail.setPrice(request.getPrice());
            ProductDetail updateProductDetail = productDetailRepository.save(detail);
            if (!request.getListImage().isEmpty()) {
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
            throw new RuntimeException("Sản phẩm chi tiết với Id là " + id + " không tồn tại trong hệ thống!");
        }
        Optional<Product> optionalProduct = productRepository.findById(optionalProductDetail.get().getProduct().getId());
        if (!optionalProduct.isPresent()) {
            throw new RuntimeException("Sản phẩm với Id là " + optionalProductDetail.get().getProduct().getId() + " không tồn tại trong hệ thống!");
        }
        if (optionalProduct.get().getStatus().equals(Status.INACTIVE.toString())) {
            throw new RuntimeException("Vui lòng bật trạng thái của sản phẩm trước khi thay đổi trạng thái");
        }
        if (optionalProductDetail.get().getQuantity() <= 0) {
            throw new RuntimeException("Vui lòng cập nhập số lượng của sản phẩm trước khi thay đổi trạng thái");
        }
        String newStatus = aBoolean ? Status.ACTIVE.toString() : Status.INACTIVE.toString();
        optionalProductDetail.get().setStatus(newStatus);
        ProductDetail productDetail = productDetailRepository.save(optionalProductDetail.get());
        notificationController.sendNotification();
        return productDetail;
    }

    public List<ProductDetailResponseByNam> findProductDetailRequests(List<Long> idProducts) {
        return productDetailRepository.findProductDetailRequests(idProducts);
    }

    public List<ProductPromotionResponse> findProductDetailActiveRequests(Long idProducts) {
        return productDetailRepository.findProductDetailActiveRequests(idProducts);
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
            String nameProduct, Long idColor, Long idSize, Long idBrand, Long idCategory, BigDecimal minPrice, BigDecimal maxPrice, Boolean gender) {
        return productDetailRepository.findProductPriceRangeWithPromotionByQuang(
                nameProduct, idColor, idSize, idBrand, idCategory, minPrice, maxPrice, gender);
    }

    public ProductPromotionResponse findProductPromotionByIdProcuctAndIdColorAndIdSize(Long idProduct, Long idColor, Long idSize) {
        Optional<ProductPromotionResponse> productPromotionResponse = productDetailRepository.findProductPromotionByIdProcuctAndIdColorAndIdSize(idProduct, idColor, idSize);
        if (productPromotionResponse.isEmpty()) {
            throw new RuntimeException("Sản phẩm đã hết hàng");
        }
        return productPromotionResponse.get();
    }

    public List<PayProductDetailResponse> findPayProductDetailByIdProductDetail(List<ProductDetailPromoRequest> productDetailPromoRequests){
        List<PayProductDetailResponse> list = new ArrayList<>();
        for (ProductDetailPromoRequest request : productDetailPromoRequests){
            PayProductDetailResponse payProductDetailResponse = productDetailRepository.findPayProductDetailByIdProductDetail(request.getIdProductDetail())
                    .orElse(null);
            if (payProductDetailResponse == null) {
                // Trả về sản phẩm không hợp lệ
                list.add(new PayProductDetailResponse(
                        request.getIdProductDetail(),request.getQuantity(),"Không tìm thấy tài nguyên sản phẩm!"));
                continue;
            }
            if (payProductDetailResponse.getQuantityProductDetail() < request.getQuantity()) {
                // Trả về thông báo số lượng không đủ
                list.add(new PayProductDetailResponse(
                        request.getIdProductDetail(),request.getQuantity(),("Sản phẩm "+payProductDetailResponse.getNameProduct()+" không đủ số lượng!")));
                continue;
            }
            payProductDetailResponse.setQuantityBuy(request.getQuantity());
            list.add(payProductDetailResponse);
        }
        return list;
    }
}
