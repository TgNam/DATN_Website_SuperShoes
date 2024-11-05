package org.example.datn_website_supershoes.service;

import lombok.RequiredArgsConstructor;
import org.example.datn_website_supershoes.dto.response.ProductDetailResponse;
import org.example.datn_website_supershoes.dto.response.ProductDetailResponseByNam;
import org.example.datn_website_supershoes.dto.response.ProductPromotionResponse;
import org.example.datn_website_supershoes.model.Product;
import org.example.datn_website_supershoes.model.ProductDetail;
import org.example.datn_website_supershoes.repository.ProductDetailRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.example.datn_website_supershoes.model.ProductImage;


import java.math.BigDecimal;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ProductDetailService {
    @Autowired
    private ProductDetailRepository productDetailRepository;

    public ProductDetail createProductDetail(ProductDetail productDetail) {

        return productDetailRepository.save(productDetail);
    }

    public Page<ProductDetailResponse> getAllProductDetail(Specification<ProductDetail> spec, Pageable pageable) {
//
        return productDetailRepository.findAll(spec, pageable).map(this::convertToProductDetailResponse);
    }

    private ProductDetailResponse convertToProductDetailResponse(ProductDetail productDetail) {
        ProductDetailResponse response = new ProductDetailResponse();

        // Chép các thuộc tính từ ProductDetail
        response.setId(productDetail.getId());
        response.setQuantity(productDetail.getQuantity() != null ? productDetail.getQuantity() : 0); // Cung cấp giá trị mặc định nếu null
        response.setPrice(productDetail.getPrice() != null ? productDetail.getPrice() : BigDecimal.ZERO); // Cung cấp giá trị mặc định nếu null
        response.setDescription(productDetail.getDescription());

        if (productDetail.getProductImage() != null) {
            response.setImageBytes(productDetail.getProductImage().stream()
                    .map(ProductImage::getImageByte)
                    .collect(Collectors.toList()));
        }

        // Kiểm tra nếu Product không bị null
        if (productDetail.getProduct() != null) {
            Product product = productDetail.getProduct();
            response.setIdProduct(product.getId());
            response.setNameProduct(product.getName());
            response.setGender(product.isGender());
            response.setProductCode(product.getProductCode());
            response.setImageByte(product.getImageByte());
            // Chuyển tiếp các giá trị từ các đối tượng liên quan
            if (product.getBrand() != null) {
                response.setIdBrand(product.getBrand().getId());
                response.setNameBrand(product.getBrand().getName());
            }
            if (product.getCategory() != null) {
                response.setIdCategory(product.getCategory().getId());
                response.setNameCategory(product.getCategory().getName());
            }
            if (product.getMaterial() != null) {
                response.setIdMaterial(product.getMaterial().getId());
                response.setNameMaterial(product.getMaterial().getName());
            }
            if (product.getShoeSole() != null) {
                response.setIdShoeSole(product.getShoeSole().getId());
                response.setNameShoeSole(product.getShoeSole().getName());
            }

        }

        // Gán các giá trị khác nếu có
        if (productDetail.getColor() != null) {
            response.setIdColor(productDetail.getColor().getId());
            response.setNameColor(productDetail.getColor().getName());
        }
        if (productDetail.getSize() != null) {
            response.setIdSize(productDetail.getSize().getId());
            response.setNameSize(productDetail.getSize().getName());
        }

        response.setStatus(productDetail.getStatus());

        return response;
    }




    public Optional<ProductDetail> getProductByIdDetail(Long id) {
        return productDetailRepository.findById(id);
    }


    public ProductDetail updateProductDetail(Long id, ProductDetail productDetail) {
        ProductDetail existingProductDetail = productDetailRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ProductDetail not found"));

        String[] ignoredProperties = {"id", "createdAt", "createdBy"};
        BeanUtils.copyProperties(productDetail, existingProductDetail, ignoredProperties);

        if (productDetail.getProduct() != null) {
            existingProductDetail.setProduct(productDetail.getProduct());
        }
        if (productDetail.getSize() != null) {
            existingProductDetail.setSize(productDetail.getSize());
        }
        if (productDetail.getColor() != null) {
            existingProductDetail.setColor(productDetail.getColor());
        }
        if (productDetail.getProductImage() != null) {
            existingProductDetail.setProductImage(productDetail.getProductImage());
        }
        if (productDetail.getPromotionDetail() != null) {
            existingProductDetail.setPromotionDetail(productDetail.getPromotionDetail());
        }
        if (productDetail.getCartDetails() != null) {
            existingProductDetail.setCartDetails(productDetail.getCartDetails());
        }
        if (productDetail.getBillDetails() != null) {
            existingProductDetail.setBillDetails(productDetail.getBillDetails());
        }

        return productDetailRepository.save(productDetail);
    }

    public void deleteProductDetail(Long id) {
        productDetailRepository.deleteById(id);
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
                    // Nếu promotionPrice không null, lọc theo promotionPrice, ngược lại lọc theo productDetailPrice
                    BigDecimal priceToFilter = ProductPromotionResponse.getPromotionPrice() != null ? ProductPromotionResponse.getPromotionPrice() : ProductPromotionResponse.getProductDetailPrice();
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

}
